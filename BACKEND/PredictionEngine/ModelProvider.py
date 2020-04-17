#!flask/bin/python
from keras.models import load_model
import pandas as pd
import numpy as np
from flask import Flask, jsonify
from flask import request
import math
from flask_cors import CORS
#http://127.0.0.1:5000/prediction/api/v1.0/task?plannedDuration=120&Category=3
app = Flask(__name__)
CORS(app)
new_model = load_model('./model/model.h5')
@app.route('/prediction/api/v1.0/task', methods=['GET'])
def get_tasks():
    dur = request.args.get('plannedDuration')
    cat = request.args.get('Category')
    # print(new_model.summary())
    # print(new_model.get_weights())
    df2 = pd.DataFrame(np.array([[dur, cat]]), columns=['Planned', 'Category'])
    Y_predKM = new_model.predict(df2)
    pred = Y_predKM[0][0]
    print(Y_predKM[0][0])
    predf = float(pred)
    durf = float(dur)
    conf = math.floor ((1+ ( durf - predf)/pred)*100)
    return jsonify({'duration': str(pred), 'confidance':str(conf)+"%"})


if __name__ == '__main__':
    app.run(host="0.0.0.0",port=5000, threaded=False)
