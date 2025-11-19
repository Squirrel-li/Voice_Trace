import whisperx
import torch
import os

print("path initialization")
path_root = os.path.dirname(os.path.abspath(__file__))
path_root = os.path.normpath(os.path.join(path_root, ".."))
path_dir_audio = os.path.join(path_root, "audio")
path_file_audio = os.path.join(path_dir_audio, "testaudio", "8000", "test01_20s.wav")

print("choosing device")
device = "cuda" if torch.cuda.is_available() else "cpu"
print(f"Using device: {device}")

print("loading models and processing audio")
# 1. WhisperX 轉寫
model = whisperx.load_model("large-v3", device)
audio = whisperx.load_audio(path_file_audio)
result = model.transcribe(audio)

print("Transcription result:")
# 2. Alignment
model_a, metadata = whisperx.load_align_model(
    language_code=result["language"], 
    device=device
)
result_aligned = whisperx.align(
    result["segments"], model_a, metadata, audio, device
)

print("Transcription result with alignment:")
# 3. Diarization (pyannote)
diarize_model = whisperx.DiarizationPipeline(
    use_auth_token="hf_xxxxxxxxxxxxxxxxxxxxx", 
    device=device
)
diarize_segments = diarize_model(path_file_audio)

print("Diarization segments:")
# 4. Speaker labeling
result_spk = whisperx.assign_word_speakers(
    diarize_segments, result_aligned
)

print("Final result with speaker labels:")
for seg in result_spk["segments"]:
    print(seg["speaker"], seg["text"])
