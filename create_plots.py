
import csv
import os
import math

import matplotlib.pyplot as plt

INPUT_FILE = "results.csv"
OUTPUT_DIR = "plots"

os.makedirs(OUTPUT_DIR, exist_ok=True)

data = []
with open(INPUT_FILE, "r") as file:
    reader = csv.DictReader(file)
    for row in reader:
        data.append({
            "algorithm": row["algorithm"],
            "n": int(row["n"]),
            "input_type": row["input_type"],
            "time_ms": float(row["time_ms"]),
            "comparisons": int(row["comparisons"]),
            "max_depth": int(row["max_depth"])
        })
algorithms = ["MergeSort", "QuickSort", "QuickSelect"]
input_types = [ "random",  "sorted","duplicates"]

 # график времени выполнения
for input_type in input_types:
    plt.figure()
    for algorithm in algorithms:
        rows = [
            row for row in data
            if row["algorithm"] == algorithm
            and row["input_type"] == input_type
        ]
        rows.sort(key=lambda x: x["n"])
        x = [row["n"] for row in rows]
        y = [row["time_ms"] for row in rows]
        plt.plot( x, y, marker="o", label=algorithm )

    plt.xlabel("Размер входных данных (n)")
    plt.ylabel("Время выполнения (мс)")
    plt.title( "Время выполнения - " + input_type)
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.savefig(
        os.path.join( OUTPUT_DIR, "time_vs_n_" + input_type + ".png" ) )

    plt.close()

# график рекурсии

for input_type in input_types:
    plt.figure()
    for algorithm in algorithms:

        rows = [
            row for row in data
            if row["algorithm"] == algorithm
            and row["input_type"] == input_type
        ]
        rows.sort(key=lambda x: x["n"])
        x = [row["n"] for row in rows]
        y = [row["max_depth"] for row in rows]
        plt.plot( x,y, marker="o", label=algorithm )

    plt.xlabel("Размер входных данных (n)")
    plt.ylabel("Максимальная глубина рекурсии")
    plt.title( "Глубина рекурсии - " + input_type )
    plt.legend()
    plt.grid(True)
    plt.tight_layout()

    plt.savefig(
        os.path.join(
            OUTPUT_DIR,
            "depth_vs_n_" + input_type + ".png"
        )
    )
    plt.close()

# график количество сравнений

for input_type in input_types:
    plt.figure()
    for algorithm in algorithms:
        rows = [
            row for row in data
            if row["algorithm"] == algorithm
            and row["input_type"] == input_type
        ]

        rows.sort(key=lambda x: x["n"])

        x = []
        ratios = []
        for row in rows:

            n = row["n"]
            comparisons = row["comparisons"]

            x.append(n)

            if algorithm == "QuickSelect":

                ratio = comparisons / n
            else:
                ratio = comparisons / (
                    n * math.log2(n)
                )

            ratios.append(ratio)
        print(
            algorithm,
            input_type,
            "x =", len(x),
            "ratios =", len(ratios)
        )


        plt.plot(
            x,
            ratios,
            marker="o",
            label=algorithm
        )


    plt.xlabel("Размер входных данных (n)")
    plt.ylabel("Ratio")

    plt.title(
        "Проверка теоретической границы - "
        + input_type
    )

    plt.legend()
    plt.grid(True)

    plt.tight_layout()

    plt.savefig(
        os.path.join(
            OUTPUT_DIR,
            "ratio_vs_n_" + input_type + ".png"
        )
    )

    plt.close()
print("Все графики успешно созданы.")