from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
from ultralytics import YOLO
import cv2
import os
import numpy as np
from PIL import Image, ImageFont, ImageDraw
import json

app = Flask(__name__)
with open('config.json') as config_file:
    config = json.load(config_file)

def load_model():
    return YOLO(config['yolo_model_path'])

def compare_ftdetect(img, folder_path):    
    template_paths = []
    for root, _, files in os.walk(folder_path):
        for file in files:
            img_path = os.path.join(root, file)
            template_paths.append(img_path)
    similarity_scores = {}

    for template_path in template_paths:
        sift = cv2.SIFT_create()

        img2 = imread(template_path)
        img2 = cv2.resize(img2, (640, 640))
        kp1, des1 = sift.detectAndCompute(img, None) 
        kp2, des2 = sift.detectAndCompute(img2, None)
        matcher = cv2.BFMatcher() 

        matches = matcher.knnMatch(des1, des2, 2)
        
        good_matches = [] 
        for m in matches: 
            if m[0].distance / m[1].distance <0.7:
                good_matches.append(m[0]) 
        similarity_scores[template_path] = sum(match.distance for match in good_matches)
    most_similar_template = max(similarity_scores, key=similarity_scores.get)
    most_similar_folder = os.path.dirname(most_similar_template)
    predictName = os.path.basename(most_similar_folder)

    return predictName

def imread(filename, flags=cv2.IMREAD_COLOR, dtype=np.uint8): 
    try: 
        n = np.fromfile(filename, dtype) 
        img = cv2.imdecode(n, flags) 
        return img 
    except Exception as e: 
        print(e) 
        return None

@app.route('/health-check', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

@app.route('/detect', methods=['POST'])
def detect():
    if 'images' not in request.files:
        return 'No images part in the request', 400

    model = load_model()
    name_folder = config['name_folder']
    images = request.files.getlist('images')
    results = []

    for image_file in images:
        filename = secure_filename(image_file.filename)
        img = cv2.imdecode(np.frombuffer(image_file.read(), np.uint8), cv2.IMREAD_UNCHANGED)

        detection_results = model.predict(img)
        detection_json = detection_results[0].tojson()
        detection_dict = json.loads(detection_json)

        for detection in detection_dict:
            x1, y1, x2, y2 = map(int, [detection['box'][key] for key in ['x1', 'y1', 'x2', 'y2']])
            cropped_img = img[y1:y2, x1:x2]
            predictName = compare_ftdetect(cropped_img, name_folder)

            results.append({
                'filename': filename,
                'object_name': predictName,
                'position': {'x1': x1, 'y1': y1, 'x2': x2, 'y2': y2}
            })

    return jsonify(results)


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
