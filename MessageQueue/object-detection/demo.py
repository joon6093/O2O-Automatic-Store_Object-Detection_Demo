from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
from datetime import datetime
import json
import pytz
from py_eureka_client import eureka_client

app = Flask(__name__)

def register_service_to_eureka():
    eureka_client.init(eureka_server="http://127.0.0.1:8761/eureka/",
                       app_name="object-detection",
                       instance_port=5000,
                       instance_ip='127.0.0.1',)

def load_demo_results():
    with open('demo_results.json') as f:
        return json.load(f)
    
@app.route('/info', methods=['GET'])
def info(): 
    kst = pytz.timezone('Asia/Seoul')
    build_time = datetime.now(kst).isoformat()
    
    info_data = {
        "app": {
            "name": "object-detection",
            "company": "iia"
        },
        "build": {
            "artifact": "object-detection",
            "name": "object-detection",
            "time": build_time, 
            "version": "0.0.1-SNAPSHOT",
            "group": "com.iia"
        }
    }
    return jsonify(info_data), 200

@app.route('/health', methods=['GET'])
def health():  
    return jsonify({"status": "UP"}), 200 

@app.route('/detect', methods=['POST'])
def detect():
    if 'images' not in request.files:
        return 'No images part in the request', 400

    images = request.files.getlist('images')
    demo_results = load_demo_results()
    results = []

    for image_file in images:
        filename = secure_filename(image_file.filename)
        
        for demo_result in demo_results:
            demo_result_modified = demo_result.copy()  
            demo_result_modified['filename'] = filename 
            results.append(demo_result_modified)

    return jsonify(results)

if __name__ == '__main__':
    register_service_to_eureka()
    app.run(debug=True, host='127.0.0.1', port=5000)
