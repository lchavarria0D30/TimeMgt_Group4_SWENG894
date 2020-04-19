from keras.layers import Dense
from keras import metrics
from keras.models import Sequential
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
colNames = ['Planned',	'Actual',	'Category']
data = pd.read_csv('./data/ScottData.csv', names=colNames)

# print(data.head(20))
# print(data.info())


# scale the data. ( x - min/(min -max))
scaler = MinMaxScaler()
DataScaled = scaler.fit_transform(data)
DataScaled = pd.DataFrame(DataScaled, columns=colNames)
summary = DataScaled.describe()
summary = summary.transpose()
print(summary)


# start with ploting after scalling
# boxplot = DataScaled.boxplot(column=BHNames)
# plt.show()
CorData = DataScaled.corr(method='pearson')
with pd.option_context('display.max_rows', None,
                       'display.max_columns', CorData.shape[1]):
    print(CorData)
    # show variable correlation.
""" plt.matshow(CorData)
plt.xticks(range(len(CorData.columns)), CorData.columns)
plt.yticks(range(len(CorData.columns)), CorData.columns)
plt.colorbar()
plt.show() """


X = data.drop('Actual', axis=1)
# print('printing x', X.head)
# print(X.describe())
# print('printing Y')
Y = data['Actual']
# print(Y.describe())


X_train, X_test, Y_train, Y_test = train_test_split(
    X, Y, test_size=0.30, random_state=5)
#print('X train shape= ', X_train.shape)
print('X test shape= ', X_test.shape)

#print('Y train shape= ', Y_train.shape)
#print('Y test shape= ', Y_test.shape)


model = Sequential()
model.add(Dense(20, input_dim=2, activation='relu'))
model.add(Dense(10, activation='relu'))
model.add(Dense(1, activation='linear'))

# configur learning process
model.compile(optimizer='adam', loss='mean_squared_error',
              metrics=['accuracy'])
# To train the model, the fit() method
# X_train: Array of predictors training data.
# Y_train: Array of target (label) data.
model.fit(X_train, Y_train, epochs=1000, verbose=0)

# print(model.summary())

Y_predKM = model.predict(X_test)

# evaluate the performance of the model
score = model.evaluate(X_test, Y_test, verbose=0)
print('Keras Model')
print(score[0])


# Save the model in h5 format
model.save("./model/model.h5")
# https://towardsdatascience.com/deploying-keras-deep-learning-models-with-java-62d80464f34a
