# 0. 사용할 패키지 불러오기
import numpy as np
import matplotlib.pyplot as plt
import cv2
import PIL
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import Flatten
from tensorflow.keras.layers import Conv2D, MaxPooling2D, Dropout
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras import backend as K


# 개별 사진 불러와 확인하기

# 1. 모델 불러오기
from tensorflow.keras.models import load_model
model = load_model('test_cabbage_class_01.h5')


# 2. 영상 불러오기


test_image = ImageDataGenerator(rescale=1./255)

test_generator = test_image.flow_from_directory(
        'C:/Users/USER/Desktop/HotSix/deeplearning/testimg3',
        target_size=(30, 30),
        class_mode='categorical')


print(test_generator)
output = model.predict_generator(test_generator)
np.set_printoptions(formatter={'float': lambda x: "{0:0.3f}".format(x)})
#print('\n', 'Test accuracy:', score[1])
#print(test_generator.class_indices)
print(output)

a=0
b=0

for i in range(len(output[0])):

    if a < output[0][i]:
        a = output[0][i]
        b = i
            
print(a, b)
filename = 'testimg_ca.jpg'
class_name = ["뿌리 혹병", "배추 역병", "무름병"]
image = cv2.imread('C:/Users/USER/Desktop/HotSix/deeplearning/testimg3/img/' + filename)
cv2.imshow("test",image)

print("%s: %.2f%%" %(class_name[b], output[0][b]*100))


 
