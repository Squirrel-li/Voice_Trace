import whisperx
import torch
import os


# initialize paths
print("path initialization")
path_root = os.path.dirname(os.path.abspath(__file__))
path_root = os.path.normpath(os.path.join(path_root, ".."))
path_dir_audio = os.path.join(path_root, "audio")
path_file_audio = os.path.join(path_dir_audio, "testaudio", "8000", "test01_20s.wav")

# check if audio file exists
if not os.path.exists(path_file_audio):
    raise FileNotFoundError(f"Audio file not found: {path_file_audio}")

# 讀取 token
token_path = os.path.join(path_root, "token.txt")
with open(token_path, "r", encoding="utf-8") as f:
    HF_TOKEN = f.read().strip()

# set device
device = "cuda" if torch.cuda.is_available() else "cpu"

# 1. WhisperX 轉寫
model = whisperx.load_model("large-v3", device, compute_type="int8")
audio = whisperx.load_audio(path_file_audio)
result = model.transcribe(audio)

# 2. Alignment
model_a, metadata = whisperx.load_align_model(
    language_code=result["language"], 
    device=device
)
result_aligned = whisperx.align(
    result["segments"], model_a, metadata, audio, device
)

# 3. Diarization (pyannote)
diarize_model = whisperx.diarize.DiarizationPipeline(
    use_auth_token=HF_TOKEN,
    device=device
)
diarize_segments = diarize_model(path_file_audio)

# 4. Speaker labeling
result_spk = whisperx.assign_word_speakers(
    diarize_segments, result_aligned
)

output_lines = []
for seg in result_spk["segments"]:
    speaker = seg.get("speaker", "UNK")
    line = f"{speaker} [{seg['start']:.2f} → {seg['end']:.2f}] {seg['text']}"
    print(line)
    output_lines.append(line)

output_txt_path = os.path.join(path_dir_audio, "transcription_result.txt")
with open(output_txt_path, "w", encoding="utf-8") as f:
    for line in output_lines:
        f.write(line + "\n")
print(f"結果已寫入 {output_txt_path}")