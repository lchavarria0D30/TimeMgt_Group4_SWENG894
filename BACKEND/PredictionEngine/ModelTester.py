from keras.models import load_model
import pandas as pd
import numpy as np
new_model = load_model('./model/model.h5')
# print(new_model.summary())
# print(new_model.get_weights())
df2 = pd.DataFrame(np.array([[100, 2]]),
                   columns=['Planned', 'Category'])
Y_predKM = new_model.predict(df2)
print(Y_predKM[0][0])
