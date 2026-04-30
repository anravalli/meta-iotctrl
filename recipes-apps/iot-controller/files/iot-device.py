#!/usr/bin/env python3

import gpiod
#import RPi.GPIO as GPIO
import paho.mqtt.client as mqtt
import time
import threading

LED_CH = 18
BUTTON_CH = 17
toggle = False

BROKER = "192.168.1.20"
PORT = 1883
STATUS_TOPIC = "iot/status/led"
COMMAND_TOPIC = "iot/command/led"

led_status = "OFF"

def buttonCallback(ch, mqtt_client):
    global toggle
    global led_status
    if button_line.get_value():
        if toggle:
            print('Switching to LOW')
            toggle = False
            led_status = "OFF"
            #GPIO.output(LED_CH, GPIO.LOW)
            led_line.set_value(0)
        else:
            toggle = True
            led_status = "ON"
            print('Switching to HIGH')
            #GPIO.output(LED_CH, GPIO.HIGH)
            led_line.set_value(1)

        mqtt_client.publish(STATUS_TOPIC, led_status)

        #debounce
        time.sleep(0.5)

def on_message(mqtt_client, userdata, msg):
    global led_status
    #decode messsage
    payload = msg.payload.decode("utf-8").upper()
    print(f"message on topic: {msg.topic}")
    match msg.topic:
        case "iot/command/led":
            print(f"received LED command: {payload}")
            if payload == "ON":
                GPIO.output(LED_CH, GPIO.HIGH)
            else:
                GPIO.output(LED_CH, GPIO.LOW)
            led_status = payload
            mqtt_client.publish(STATUS_TOPIC, led_status)
        case _:
            print(f"Unsupported topic")
            print(f"...payload was: payload")



def gpioSetUp():
    global chip, led_line, button_line

    print("Setting up GPIO (gpiod)")

    chip = gpiod.Chip("gpiochip0")

    led_line = chip.get_line(LED_CH)
    button_line = chip.get_line(BUTTON_CH)

    led_line.request(consumer="iot-led", type=gpiod.LINE_REQ_DIR_OUT)
    button_line.request(
        consumer="iot-button",
        type=gpiod.LINE_REQ_DIR_IN,
        flags=gpiod.LINE_REQ_FLAG_BIAS_PULL_DOWN
    )

def setupMqttClient():
    client = mqtt.Client()

    def on_connect(mqtt_client, userdata, flags, rc):
        if rc == 0:
            print("Connected to Broker!")
            print(f"subscribing to {COMMAND_TOPIC}")
            client.subscribe(COMMAND_TOPIC)
            mqtt_client.publish(STATUS_TOPIC, led_status)
        else:
            print(f"Connection error, code: {rc}")

    client.on_connect = on_connect
    client.on_message = on_message
    # Connessione
    print(f"Connecting to {BROKER}...")
    client.connect_async(BROKER, PORT, 60)

    client.loop_start()

    return client

try:
    print("Remote Pi Control Switch")
    gpioSetUp()

    #init LED state
    GPIO.output(LED_CH, GPIO.LOW)

    client = setupMqttClient()

#    GPIO.add_event_detect(BUTTON_CH, GPIO.RISING, callback=buttonCallback)

    print("...waiting for button event...")
#    GPIO.wait_for_edge(channel, GPIO.RISING)
    while True:
        buttonCallback(BUTTON_CH, client)
        #client.loop()
        time.sleep(0.05)

except KeyboardInterrupt:
    print("\n...loop stopped...")

finally:
    led_line.release()
    button_line.release()
    print("GPIO cleanup and exit.")
