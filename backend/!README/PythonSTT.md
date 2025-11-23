# WhisperX + pyannote 做多講者辨識

## 1. 環境建置

### 1.1 版本

-python : 3.10.11

### 1.2 套件下載

1. 下載 Whisper (OpenAI Whisper), PyTorch, pyannote 相關依賴

```powerShell
pip install whisperx
```

1. 下載 GPU 版 PyTorch (20系列以前)

```powerShell
pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118
```

1. 下載 GPU 版 PyTorch (20系列以前)

```powerShell
pip install torch --index-url https://download.pytorch.org/whl/cu118
```

1. 下載 GPU 版 PyTorch (30系列以後)

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

## 2. 測試指令

### 2.1 測試 flask 伺服器

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:5000/api/tts `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body '{
    "path_file_input":"D:/!universitiy/3-1/ApplicationSoftwareDesignPractice/final_project/project_kotlin/database/upload_audio/test01_20s.wav"}'
```

### 2.2 測試 kotlin 伺服器

```powershell
Invoke-RestMethod -Method POST -Uri http://localhost:8080/api/tts `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body '{
    "path_file_input":"D:/!universitiy/3-1/ApplicationSoftwareDesignPractice/final_project/project_kotlin/database/upload_audio/test01_20s.wav"
	}'
```