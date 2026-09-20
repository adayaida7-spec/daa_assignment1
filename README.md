# DAA Assignment 1

## Fast Sorting & Selection Engine


- MergeSort
- QuickSort
- QuickSelect

The project includes unit tests, benchmarking and performance analysis.

## Algorithms

### MergeSort

Time complexity: **Θ(n log n)**

### QuickSort

Average complexity: **Θ(n log n)**

Worst-case complexity: **Θ(n²)**

### QuickSelect

Average complexity: **Θ(n)**

Worst-case complexity: **Θ(n²)**

## Project Structure

- **algorithms/** — MergeSort, QuickSort and QuickSelect
- **benchmark/** — benchmark program
- **utils/** — metrics for comparisons and recursion depth
- **tests/** — JUnit tests
- **results.csv** — benchmark results
- **plots/** — performance graphs
- **REPORT.md** — project report

## Testing

The algorithms were tested with:

- random arrays
- sorted arrays
- arrays with duplicates
- empty arrays
- one-element arrays

The sorting results were compared with Java's `Arrays.sort`.

## Benchmark

The benchmark uses four input sizes:

- 1,000
- 10,000
- 100,000
- 1,000,000

Three input types were used:

- random
- sorted
- duplicates

Each case was run 5 times. The median execution time was saved in `results.csv`.

## Results

The benchmark results are stored in **results.csv**.

The graphs show:

- execution time
- maximum recursion depth
- comparison ratio

The graphs are stored in the **plots/** folder.

## How to Run

Run the tests with:

`mvn test`

Run `BenchmarkRunner.java` to generate the benchmark results.

Then run:

`python3 create_plots.py`

The generated graphs will be saved in the **plots/** folder.