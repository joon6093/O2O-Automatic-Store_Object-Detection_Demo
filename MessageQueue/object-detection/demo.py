from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
from kafka import KafkaProducer
import json
import pytz
from datetime import datetime
from py_eureka_client import eureka_client
import logging

app = Flask(__name__)

logging.basicConfig(level=logging.INFO, 
                    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
                    filename='demo.log',
                    filemode='a')

logger = logging.getLogger(__name__)

eureka_client.init(eureka_server="127.0.0.1:8761/eureka/",
                    app_name="object-detection",
                    instance_port=5000,
                    instance_ip='127.0.0.1',)

kafka_producer = KafkaProducer(
    bootstrap_servers="127.0.0.1:9092", 
    value_serializer=lambda x: json.dumps(x).encode('utf-8')
)

KAFKA_TOPIC = 'object-detection-results'

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
    
    store_id = request.headers.get('Store-ID')
    if not store_id:
        return 'Store-ID header is missing', 400
    
    images = request.files.getlist('images')
    demo_results = load_demo_results()
    item_summary = {}
    for image_file in images:
        filename = secure_filename(image_file.filename)
        for demo_result in demo_results:
            item_name = demo_result['object_name']
            if item_name in item_summary:
                item_summary[item_name]['count'] += 1
            else:
                item_summary[item_name] = {'count': 1}
        
        kafka_message = {
                'store_id': store_id,
                'items': item_summary
                }
        try:
            future = kafka_producer.send(topic=KAFKA_TOPIC, value=kafka_message)
            future.add_callback(lambda metadata: logger.info(f"Message sent to {metadata.topic} partition {metadata.partition} file {filename}"))
            future.add_errback(lambda e: logger.error(str(e)))
        except Exception as e:
            logger.error(f"Failed to send message to Kafka: {str(e)}")

    return jsonify({"status": "OK"}), 200

if __name__ == '__main__':
    app.run(debug=True, host='127.0.0.1', port=5000)