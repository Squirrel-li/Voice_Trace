# WhisperX + pyannote 做多講者辨識
## 1. 環境建置

### 1.1 版本
-python : 3.10.11

### 1.2 套件下載
1. 下載 Whisper (OpenAI Whisper), PyTorch, pyannote 相關依賴
```powerShell
pip install pip install whisperx
```

1. 下載 GPU 版 PyTorch
```powerShell
pip install torch --index-url https://download.pytorch.org/whl/cu121
```

1. 下載 CPU 版 PyTorch
```powerShell
pip install torch --index-url https://download.pytorch.org/whl/cpu
```

1. 下載 ffmpeg
```powerShell
winget install Gyan.FFmpeg
```

## 2. 常用函式

## 2.1 