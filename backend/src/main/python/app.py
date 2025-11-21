from flask import Flask, request, render_template, jsonify
from voiceTrace.main import VoiceTrace
import os

app = Flask(__name__)

def get_root_path():
    path_root = os.path.dirname(os.path.abspath(__file__))
    path_root = os.path.normpath(os.path.join(path_root, ".."))
    return path_root

@app.route("/api/tts", methods=["POST"])
def TTS():
    data = request.get_json()
    print("收到的資料：", data)
    path_file_input = data.get("path_file_input", "")
    path_file_token = "D:\\!universitiy\\3-1\\ApplicationSoftwareDesignPractice\\final_project\\project_kotlin\\backend\\token.txt"
    path_file_input = os.path.normpath(path_file_input)
    path_file_token = os.path.normpath(path_file_token)

    print("<path_file_input>:", path_file_input)
    print("<path_file_token>:", path_file_token)
    voice_trace = VoiceTrace(path_file_token)
    result_json = voice_trace(path_file_input)

    if result_json.get("results") is None:
        return jsonify({"error": f"Audio file not found.{path_file_input}"}), 404

    print("result_json:", result_json)

    return jsonify(result_json)

    

if __name__ == "__main__":
    app.run(debug=True, port=5000)