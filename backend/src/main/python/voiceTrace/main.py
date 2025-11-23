import whisperx
import torch
import os
torch.backends.cuda.matmul.allow_tf32 = True
torch.backends.cudnn.allow_tf32 = True

def get_root_path():
    path_root = os.path.dirname(os.path.abspath(__file__))
    path_root = os.path.normpath(os.path.join(path_root, ".."))
    return path_root

modelName = ["tiny", "base", "small", "medium", "large"]

class VoiceTrace:
    def __init__(self, path_token):
        path_token = os.path.normpath(path_token)
        self.token = self.load_token(path_token)
        self.model, self.device = self.load_models()

    def __call__(self, path_file_audio, **kwds):
        '''
        result_json: {
            'speakers': ['SPEAKER_01', 'SPEAKER_00'], 
            'results': [{'speaker': 'SPEAKER_01', 'start': 1.955, 'end': 8.488, 'text': ' Dancing in the masquerade, idle truth and plain sight jaded, pop, roll, click, shot.'}, 
                        {'speaker': 'SPEAKER_01', 'start': 8.508, 'end': 10.452, 'text': 'Who will I be today or not?'}, 
                        {'speaker': 'SPEAKER_00', 'start': 10.593, 'end': 15.402, 'text': 'But such a tide as moving seems asleep, too full for sound and foam.'}, 
                        {'speaker': 'SPEAKER_00', 'start': 15.422, 'end': 22.056, 'text': 'When that which drew from out the boundless deep turns again home, twilight and evening bell and after that'}]
        }
        return result_json

        '''
        # 1. Transcription
        res_json = {"speakers": [], "results": []}
        if not os.path.exists(path_file_audio):
            res_json["results"] = None
            return res_json
        audio = whisperx.load_audio(path_file_audio)
        result = self.model.transcribe(audio)
        # 2. Alignment
        model_a, metadata = whisperx.load_align_model(
            language_code=result["language"], 
            device=self.device
        )
        result_aligned = whisperx.align(
            result["segments"], model_a, metadata, audio, self.device
        )

        # 3. Diarization (pyannote)
        diarize_model = whisperx.diarize.DiarizationPipeline(
            use_auth_token=self.token,
            device=self.device
        )
        diarize_segments = diarize_model(path_file_audio)

        # 4. Speaker labeling
        result_spk = whisperx.assign_word_speakers(
            diarize_segments, result_aligned
        )

        '''
        output_lines = []
        for seg in result_spk["segments"]:
            speaker = seg.get("speaker", "UNK")
            line = f"{speaker} [{seg['start']:.2f} --> {seg['end']:.2f}] {seg['text']}"
            print(line)
            output_lines.append(line)

        self.check_path(os.path.dirname(path_output))
        with open(path_output, "w", encoding="utf-8") as f:
            for line in output_lines:
                f.write(line + "\n")
        '''

        for result in result_spk["segments"]:
            spk = result.get("speaker", "UNK")
            if spk not in res_json["speakers"]:
                res_json["speakers"].append(spk)
            res_json["results"].append({
                "speaker": spk,
                "start": result["start"],
                "end": result["end"],
                "text": result["text"]
            })
        
        return res_json
            

    def check_path(self, path):
        path = os.path.normpath(path)
        if not os.path.exists(path):
            os.mkdir(path)

    def load_models(self, model_name=modelName[2], compute_type="float32"):
        device = "cuda" if torch.cuda.is_available() else "cpu"
        print(f"Using device: {device}")
        model = whisperx.load_model(model_name, device, compute_type=compute_type)
        return model, device
        
    def load_token(self, token_path):
        with open(token_path, "r", encoding="utf-8") as f:
            HF_TOKEN = f.read().strip()
        return HF_TOKEN
        


def main():
    # initialize paths
    path_root = get_root_path()
    path_dir_audio = os.path.join(path_root, "audio")
    path_file_audio = os.path.join(path_dir_audio, "testaudio", "8000", "test01_20s.wav")
    path_output = os.path.join(path_root, "output", "transcription_result.txt")

    token_path = os.path.join(path_root, "token.txt")

    voice_trace = VoiceTrace(token_path)
    result_json = voice_trace(path_file_audio, path_output)
    print(result_json)
    print("VoiceTrace main function executed.")

if __name__ == "__main__":
    main()