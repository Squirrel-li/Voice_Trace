from flask import Flask, request, jsonify
import threading
import requests
from voiceTrace.main import VoiceTrace
import os
import base64
import jwt
import json  # 用於將列表轉換為 JSON 字串

app = Flask(__name__)

def get_root_path():
    path_root = os.path.dirname(os.path.abspath(__file__))
    path_root = os.path.normpath(os.path.join(path_root, ".."))
    return path_root

def notify_kotlin_backend(result_json, file_id):
    url = "http://127.0.0.1:8080/api/speech/result"  # 確保這是 Kotlin 的正確端點
    payload = {
        "speakers": result_json.get("speakers", []),  # 確保 speakers 是列表
        "results": result_json.get("results", []),    # 確保 results 是列表
        "file_id": file_id
    }  # 將處理結果和 file_id 作為 JSON 發送
    try:
        resp = requests.post(url, json=payload, timeout=10)  # 發送 POST 請求
        print("Kotlin 回應：", resp.text)
    except Exception as e:
        print("通知 Kotlin 失敗：", e)


def process_voice_trace(path_file_input, path_file_output, file_id):
    """執行 VoiceTrace 並通知 Kotlin 後端"""
    try:
        print("開始處理 VoiceTrace...")
        result_json = voice_trace(path_file_input)

        # 檢查結果是否存在
        if result_json.get("results") is None:
            print(f"處理失敗，音訊檔案未找到：{path_file_input}")
            return

        # 確保 results 是字串格式
        results = result_json["results"]
        transcripts = ""
        for result in results:
            transcripts += f"{result.get('speaker', '')}: \t"
            transcripts += f"{result.get('start', 0):.1f} - {result.get('end', 0):.1f} \t"
            transcripts += f"{result.get('text', '')}\n"

        # 將結果寫入輸出檔案
        with open(path_file_output, "w", encoding="utf-8") as f:
            f.write(transcripts)  # 寫入字串
        print(f"處理完成，結果已寫入檔案：{path_file_output}")

        # 添加 file_id 到 result_json

        # 通知 Kotlin 後端
        notify_kotlin_backend(result_json, file_id)

    except Exception as e:
        print(f"處理 VoiceTrace 時發生錯誤：{e}")


def process_voice_trace_async(path_file_input, path_file_output, file_id):
    threading.Thread(target=process_voice_trace, args=(path_file_input, path_file_output, file_id)).start()

@app.route("/api/tts", methods=["POST"])
def TTS():
    try:
        raw_data = request.data.decode("utf-8")
        data = request.get_json()

        if not data:
            return jsonify({"error": "請求體為空或格式錯誤"}), 400

        path_file_input = data.get("path_file_input", "")
        path_file_output = data.get("path_file_output", "")
        file_id = data.get("file_id", "")
        if not path_file_input:
            return jsonify({"error": "缺少 path_file_input"}), 400
        if not path_file_output:
            return jsonify({"error": "缺少 path_file_output"}), 400

        print("<path_file_input>:", path_file_input)
        print("啟動背景執行緒處理 VoiceTrace...")
        process_voice_trace_async(path_file_input, path_file_output, file_id)
        print("已啟動背景執行緒處理 VoiceTrace")
        return jsonify({"status": "success", "message": "已接收到請求"}), 200
    except Exception as e:
        print("處理請求時發生錯誤：", e)
        return jsonify({"error": str(e)}), 500


path_file_token = "D:\\!universitiy\\3-1\\ApplicationSoftwareDesignPractice\\final_project\\project_kotlin\\backend\\token.txt"
path_file_token = os.path.normpath(path_file_token)
voice_trace = VoiceTrace(path_file_token)

if __name__ == "__main__":
    print("\n\n\nStarting Flask server on port 5000...")
    app.run(debug=True, port=5000)