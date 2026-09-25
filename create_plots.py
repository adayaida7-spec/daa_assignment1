
from pathlib import Path
import csv
import math

import matplotlib
matplotlib.use("Agg")

import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator


BASE_DIR = Path(__file__).resolve().parent
OUTPUT_DIR = BASE_DIR / "plots"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

with (BASE_DIR / "results.csv").open(
    encoding="utf-8", newline=""
) as file:
    rows = list(csv.DictReader(file))

assert len(rows) == 36
assert len({
    (row["algorithm"], row["input_type"], row["n"])
    for row in rows
}) == 36

ALGORITHMS = ["MergeSort", "QuickSort", "QuickSelect"]

COLORS = {
    "MergeSort": "#2474b5",
    "QuickSort": "#d8660d",
    "QuickSelect": "#248d53",
}

INPUT_TYPES = [
    ("random", "Случайные значения"),
    ("sorted", "Отсортированный массив"),
    ("duplicates", "Повторы: значения 0–9"),
]

plt.rcParams.update({
    "font.size": 10,
    "axes.spines.top": False,
    "axes.spines.right": False,
})

PLOTS = [
    (
        "01_time",
        "Время выполнения: медиана 5 запусков",
        ALGORITHMS,
        lambda row: float(row["time_ms"]),
        "Время, мс",
        True,
    ),
    (
        "02_stack_depth",
        "Медиана максимальной глубины рекурсивных вызовов",
        ["MergeSort", "QuickSort"],
        lambda row: int(row["max_depth"]),
        "Максимальная глубина",
        False,
    ),
    (
        "03_selection_iterations",
        "QuickSelect: медиана числа итераций разбиения",
        ["QuickSelect"],
        lambda row: int(row["iterations"]),
        "Итерации, не глубина стека",
        False,
    ),
    (
        "04_sort_comparisons",
        "Сортировки: нормированное число сравнений",
        ["MergeSort", "QuickSort"],
        lambda row: int(row["comparisons"]) / (
            int(row["n"]) * math.log2(int(row["n"]))
        ),
        "C / (n · log₂ n)",
        False,
    ),
    (
        "05_select_comparisons",
        "QuickSelect: нормированное число сравнений",
        ["QuickSelect"],
        lambda row: int(row["comparisons"]) / int(row["n"]),
        "C / n",
        False,
    ),
]

for name, title, algorithms, get_value, ylabel, log_y in PLOTS:
    figure, axes = plt.subplots(1, 3, figsize=(15, 4.6))

    for axis, (input_type, input_title) in zip(axes, INPUT_TYPES):
        for algorithm in algorithms:
            selected = sorted(
                [
                    row for row in rows
                    if row["algorithm"] == algorithm
                    and row["input_type"] == input_type
                ],
                key=lambda row: int(row["n"]),
            )

            x = [int(row["n"]) for row in selected]
            y = [get_value(row) for row in selected]

            axis.plot(
                x,
                y,
                "o-",
                color=COLORS[algorithm],
                label=algorithm,
                linewidth=2,
            )

            if len(algorithms) == 1:
                for x_value, y_value in zip(x, y):
                    label = (
                        f"{y_value:.2f}"
                        if "comparisons" in name
                        else str(int(y_value))
                    )

                    axis.annotate(
                        label,
                        (x_value, y_value),
                        xytext=(0, 8),
                        textcoords="offset points",
                        ha="center",
                        fontsize=9,
                    )

        axis.set_xscale("log")
        axis.set_xticks(
            [1_000, 10_000, 100_000, 1_000_000],
            ["1 000", "10 000", "100 000", "1 000 000"],
        )

        if log_y:
            axis.set_yscale("log")
        else:
            maximum = max(
                max(line.get_ydata()) for line in axis.lines
            )
            axis.set_ylim(0, maximum * 1.22)

        if "depth" in name or "iterations" in name:
            axis.yaxis.set_major_locator(
                MaxNLocator(integer=True)
            )

        axis.set_title(input_title)
        axis.set_xlabel("Размер массива n")
        axis.set_ylabel(ylabel)
        axis.grid(alpha=0.25)
        axis.legend(fontsize=9)

    figure.suptitle(title, fontsize=15)

    figure.text(
        0.5,
        0.025,
        "Источник: results.csv. Каждая метрика — отдельная "
        "медиана 5 запусков после 5 прогревов.",
        ha="center",
        fontsize=8,
    )

    figure.tight_layout(rect=(0, 0.07, 1, 0.94))
    figure.savefig(OUTPUT_DIR / f"{name}.png", dpi=160)
    plt.close(figure)

print("Созданы 5 графиков по данным results.csv.")