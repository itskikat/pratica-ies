#!/usr/bin/env python
import pika
import sys
import argparse
import numpy as np
import json
import time
import asyncio
import os
import requests
import random
from random import randint

#---------------------------------------------------------------------

class Generator:

    def __init__(self, heartbeat):
        self.hearbeat = heartbeat
        # rabbit connections // os.getenv('RABBITMQ_IP')
        #self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.getenv('RABBITMQ_IP')))
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host='192.168.160.217', port=5672))
        self.channel = self.connection.channel()
        self.sensor_id = 1

    async def get_sensors(self):
        while True:
            #sensors = requests.get('http://'+os.getenv('SERVER_IP')+':8080/api/sensors/ids')
            sensors = requests.get('http://192.168.160.217:8080/api/sensors/ids')
            lst = sensors.json()
            print(os.getenv('RABBITMQ_IP'))
            print(os.getenv('SERVER_IP'))
            high = lst[len(lst) - 1]
            self.sensor_id = randint(1, high)
            await asyncio.sleep(2)
    

    async def gen_heart_beats(self, heartbeat=80):
        mu = heartbeat
        sigma = 10.0

        while True:
            hb = np.random.randn(1) * sigma + mu
            json_text = {'id': self.sensor_id, 'heartbeat': int(hb)}
            self.channel.basic_publish(exchange='logs', routing_key='heart_beat', body= json.dumps(json_text))
            await asyncio.sleep(2)


    async def gen_blood_pressure(self):
        systolic_mu = 130
        diastolic_mu = 85
        sigma = 20.0

        while True:
            systolic = np.random.randn(1) * sigma + systolic_mu
            diastolic = np.random.randn(1) * sigma + diastolic_mu            
            json_text = {'id': self.sensor_id, 'systolic': round(float(systolic),2), 'diastolic': round(float(diastolic),2)}
            self.channel.basic_publish(exchange='logs', routing_key='blood_pressure', body= json.dumps(json_text))
            await asyncio.sleep(20)

    
    async def gen_body_temp(self):
        mu = 37
        sigma = 1.0

        while True:
            temperature = np.random.randn(1) * sigma + mu        
            json_text = {'id': self.sensor_id, 'temperature': round(float(temperature),2)}
            self.channel.basic_publish(exchange='logs', routing_key='body_temp', body= json.dumps(json_text))
            await asyncio.sleep(20)

    
    async def gen_sugar_level(self):
        mu = 5.0
        sigma = 1.0

        while True:
            sugar = np.random.randn(1) * sigma + mu
            json_text = {'id': self.sensor_id, 'sugar': float(sugar)}
            self.channel.basic_publish(exchange='logs', routing_key='sugar_level', body= json.dumps(json_text))
            await asyncio.sleep(20)

    async def gen_oxygen_level(self):
        vals = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14]
        weights = [0.15,0.2,0.15,0.12,0.12,0.1,0.06,0.0125,0.0125,0.0125,0.0125,0.0125,0.0125,0.0125,0.0125]


        while True:
            oxygen = 100 - (random.choices(vals,weights)[0]+(random.random()**2))
            json_text = {'id': self.sensor_id, 'oxygen': float(oxygen)}
            self.channel.basic_publish(exchange='logs', routing_key='oxygen_level', body= json.dumps(json_text))
            await asyncio.sleep(2)

    




if __name__ == "__main__":

    # arguments (maybe)
    parser = argparse.ArgumentParser()
    # normal heart beat argument
    parser.add_argument("--hb", help="Normal heart beat", type=int, default=80)
    args = parser.parse_args()
    
    # generator class
    g = Generator(args.hb)


    # asyncio to send all at once
    #loop
    loop = asyncio.get_event_loop()

    #tasks
    sensor_task = loop.create_task(g.get_sensors())

    hb_task = loop.create_task(g.gen_heart_beats())
    bp_task = loop.create_task(g.gen_blood_pressure())
    bt_task = loop.create_task(g.gen_body_temp())
    sl_task = loop.create_task(g.gen_sugar_level())
    ol_task = loop.create_task(g.gen_oxygen_level())

    # run them
    loop.run_until_complete(asyncio.gather(hb_task, bp_task, bt_task, sl_task, ol_task, sensor_task))

    #loop close
    loop.close()
