from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import json

app = Flask(__name__)

def load_demo_results():
    with open('demo_results.json') as f:
        return json.load(f)

@app.route('/health-check', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

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
    app.run(debug=True, host='0.0.0.0', port=5000)
