import csv

def getWeightEstimates(filename, sex):
    data = list(csv.reader(open(filename)))
    weights_list = []
    for row_num, row in enumerate(data):
        if row_num != 0:
            wingspan = row[0]
            for col_num, weight in enumerate(row):
                if col_num != 0 and weight is not None and weight != "":
                    length = data[0][col_num]
                    weights_list.append([sex, length, wingspan, weight])
    return weights_list

weights_list = []
weights_list += getWeightEstimates("WEIGHTTABLES2003_m.csv", "M")
weights_list += getWeightEstimates("WEIGHTTABLES2003_f.csv", "F")

outputs_list = []
for i, vals in enumerate(weights_list, start=1):
    outputs_list.append([i, vals[0], vals[1], vals[2], vals[3]])

with open("weights.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerows(outputs_list)
