HW7
===

程式執行方法
java IKDDhw7 \<input_data_filename\> \<probability\> \<Iteration_num\>

共三個參數 分別為 
1.待測資料路徑 
2.傳播機率(0 < p < 1)預設為0.1
3.Monte Carlo simulation 迭代次數預設為1000

此程式以作業為目的，針對http://snap.stanford.edu/data/loc-brightkite.html 資料設計，
故不同待測資料必須像似於上述資料集，即必須擁有10個以上連通子圖，且不能有隔點(點編號必須連續)
