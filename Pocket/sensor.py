import Adafruit_BBIO.ADC as ADC
import Adafruit_BBIO.GPIO as GPIO
import time
from firebase import firebase
from datetime import datetime
import json
import httplib, urllib

#Conexion a la base
firebase = 
firebase.FirebaseApplication('https://si-micros-control.firebaseio.com/', 
None)

sensor_pin = "P1_25"
ADC.setup()
GPIO.setup("USR0",GPIO.OUT)
GPIO.setup("USR1",GPIO.OUT)
GPIO.setup("USR2",GPIO.OUT)
GPIO.setup("USR3",GPIO.OUT)
GPIO.setup("P1_10",GPIO.OUT)

id = 0
#Funcion para registrar en firebase los registros
def registrar(humedad, accion,id):
    now = datetime.now()
    t = now.strftime("%m-%d-%Y %H:%M:%S")
    ids = str(id) + ""

    nuevo =    {'id'+ids:
                        {'datetime': t ,
                        'value': humedad,
                        'action': accion}
                    }

    res = firebase.patch('/regHum', nuevo)


print('Porcentaje de Humedad\t\tVolts')

while True:
    regar_ahora = firebase.get('/regar', None)
    regar_ahora = int(regar_ahora)
    if regar_ahora == 1:

        print("Regando ahora...")

        reading = ADC.read(sensor_pin)
        volts = reading * 3.3000
        #Conversion a porcentaje
        humidity = ((3.3-volts)/3.3)*100
        humedad_minima = firebase.get('/minHum', None)

        k='L6QXG39HIUMNMP2D'
        params = urllib.urlencode({'field1': humidity, 'field2': 
humedad_minima, 'key': k})
        headers = {"Content-type": 
"application/x-www-form-urlencoded","Accept":"text/plain"}
        conn = httplib.HTTPConnection("api.thingspeak.com:80")
        conn.request("POST", "/update", params, headers)

        GPIO.output('P1_10', GPIO.HIGH)
        GPIO.output('USR0', GPIO.LOW)
        GPIO.output('USR1', GPIO.HIGH)
        GPIO.output('USR2', GPIO.LOW)
        GPIO.output('USR3', GPIO.HIGH)
        time.sleep(0.5)
        GPIO.output('USR0', GPIO.HIGH)
        GPIO.output('USR1', GPIO.LOW)
        GPIO.output('USR2', GPIO.HIGH)
        GPIO.output('USR3', GPIO.LOW)
        registrar(humidity, '1',id)
    else:

        GPIO.output('P1_10', GPIO.LOW)
        GPIO.output('USR0', GPIO.LOW)
        GPIO.output('USR1', GPIO.LOW)
        GPIO.output('USR2', GPIO.LOW)
        GPIO.output('USR3', GPIO.LOW)

        #Obtiene la humedad minima de la base
        humedad_minima = firebase.get('/minHum', None)
        #Lectura de SDC
        reading = ADC.read(sensor_pin)
        volts = reading * 3.3000
        #Conversion a porcentaje
        humidity = ((3.3-volts)/3.3)*100

        #CONFIRGURACION DE THINGSPEAK

        k='L6QXG39HIUMNMP2D'
        params = urllib.urlencode({'field1': humidity, 'field2': 
humedad_minima, 'key': k})
        headers = {"Content-type": 
"application/x-www-form-urlencoded","Accept":"text/plain"}
        conn = httplib.HTTPConnection("api.thingspeak.com:80")
        conn.request("POST", "/update", params, headers)

        #IMPRESION EN CONSOLA
        print('%f\t%f' % (humidity, volts))
        id = id +1

        if humidity < humedad_minima:
            registrar(humidity, '1',id)
            GPIO.output('P1_10', GPIO.HIGH)

            GPIO.output('USR0', GPIO.HIGH)
            GPIO.output('USR1', GPIO.HIGH)
            GPIO.output('USR2', GPIO.HIGH)
            GPIO.output('USR3', GPIO.HIGH)

        else:
            registrar(humidity, '0',id)
            GPIO.output('P1_10', GPIO.LOW)
            GPIO.output('USR0', GPIO.LOW)
            GPIO.output('USR1', GPIO.LOW)
            GPIO.output('USR2', GPIO.LOW)
            GPIO.output('USR3', GPIO.LOW)
        time.sleep(3)

