import kotlin.math.abs
import kotlin.system.exitProcess

private const val ROTATIONAL_DELAY = 4.17
private const val TRANSFER_DELAY = 0.13
private const val START_AND_STOP_DELAY = 1.0
private const val CYLINDER_DIVISIONS = 4000

/*
 * Implementation Note:
 *
 * This is not a "clean" implementation as it's written with below shortcomings:
 * + It's not quite "Kotlin idiomatic".
 * + It made use of shared mutable state.
 * + It could make use of enum instead of integer to determine direction.
 *
 * However, for the sole purpose of a quick comparison, it's kept as is.
 */
class ScanAndFcfsComparator {
    private var currentLocation = 0.0
    private var currentLocationFirst = 0.0

    private var time = mutableListOf<Double>()
    private var entrance = mutableListOf<List<Double>?>()
    private var index = 0
    private var requests = 0
    private var direction = 1

    private var elevatorRequests = mutableListOf<List<Double>?>()
    private var elevatorDone = mutableListOf<List<Double>?>()
    private var elevatorAverage = 0.0

    private var fcfsRequests = mutableListOf<List<Double>?>()
    private var fcfsDone = mutableListOf<List<Double>?>()
    private var fcfsAverage = 0.0


    fun start() {
        fun printMainMenu() = section("Disk Scheduling Simulator") {
            listOf(
                "1" to "Elevator & FCFS Algorithm [Manual Input]",
                "2" to "Elevator & FCFS Algorithm [Random Input]",
                "3" to "Quit",
            ).forEach { (code, text) -> option(code, text) }
        }

        fun navigate(input: Int) = when (input) {
            1 -> compareAlgorithmsManually()
            2 -> compareAlgorithmsRandomly()
            3 -> exitProcess(0)
            else -> error("Invalid input, possible options: 1, 2, 3")
        }

        while (true) {
            printMainMenu()
            navigate(readInt())
        }
    }

    private fun copyList() {
        fcfsRequests.addAll(elevatorRequests)
        entrance.addAll(elevatorRequests)
        time.sort()
    }

    private fun compareAlgorithmsManually() {
        fun setInputsManually() {
            subTitle("Disk State")
            currentLocation = Ask.double("Current Location:")
            currentLocationFirst = currentLocation
            requests = Ask.int("Count of IO Requests:")
            subTitle("Request's Cylinder & First Time Available")
            for (i in 0 until requests) {
                val section = buildList {
                    add(Ask.double("Request ${i + 1}'s Cylinder:"))
                    add(Ask.double("Request ${i + 1}'s First Time Available:"))
                }
                elevatorRequests.add(section)
                time.add(section.first())
                info("Added:")
                info("X${i + 1}: ${section[0]}", "T${i + 1}: ${section[1]}")
            }
            copyList()
        }

        setInputsManually()

        currentLocation = currentLocationFirst
        runElevatorAlgorithm()

        currentLocation = currentLocationFirst
        runFcfsAlgorithm()
    }

    private fun compareAlgorithmsRandomly() {
        fun setInputsRandomly() {
            val max = 10000
            val maxTime = 100
            val min = 1000
            val minTime = 0
            requests = Ask.int("Enter the number of random requests:")
            val set = mutableSetOf<Double>()
            while (set.size < requests) {
                val randomNumber = Math.random() * (max - min + 1) + min
                set.add(randomNumber)
            }
            for (aDouble in set) {
                val section = buildList {
                    add(aDouble)
                    add(Math.random() * (maxTime - minTime + 1) + minTime)
                }
                elevatorRequests.add(section)
                time.add(section[1])
            }
            copyList()
        }

        setInputsRandomly()

        currentLocation = 1000.0
        runElevatorAlgorithm()

        currentLocation = 1000.0
        runFcfsAlgorithm()

        println()
        info("Elevator's Average: %.2f".format(elevatorAverage), "FCFS's Average: %.2f".format(fcfsAverage))
        println()
    }

    private fun sort(list: MutableList<List<Double>?>, pos: Int) {
        for (i in 0 until requests) {
            for (j in i + 1 until requests) {
                if (list[i]!![pos] <= list[j]!![pos]) continue
                val temp = list[i]
                list[i] = list[j]
                list[j] = temp
            }
        }
    }

    private fun average(list: MutableList<List<Double>?>): Double = list.sumOf { it!![1] } / list.size
    private fun clearElevator() = listOf(elevatorRequests, elevatorDone, time).onEach { it.clear() }
    private fun clearFcfs() = listOf(fcfsRequests, fcfsDone, entrance).onEach { it.clear() }
    private fun List<List<Double>?>.formatFirstsAndSeconds() = joinToString(separator = "\n") {
        "%.2f".format(it!![0]).padEnd(20).purpleBright().reset() +
                " ".bgGreenBright().blackBold().reset() +
                "%.2f".format(it[1]).padStart(20).blueBoldBright().reset()
    }

    /**
     * The Elevator Algorithm. It processes the requests in its moving direction until it reaches the end of the disk.
     * Then, the disk's direction is reversed and the requests in the opposite direction are served.
     */
    private fun runElevatorAlgorithm() {
        info("Entrance:")
        info(entrance.formatFirstsAndSeconds())
        sort(elevatorRequests, 0)
        var doneAtTime = 0.0
        for (i in 0 until requests) {
            if (elevatorRequests[i]!![1] != time[0]) continue
            index = i
            break
        }
        while (isEmpty) {
            doneAtTime = when {
                direction == 1 && checkTime(doneAtTime) -> upList(doneAtTime)
                direction == 0 && checkTime(doneAtTime) -> downList(doneAtTime)
                direction == 1 && !checkTime(doneAtTime) -> upListTime(doneAtTime)
                else -> downListTime(doneAtTime)
            }
        }
        elevatorAverage = average(elevatorDone)
        info("Elevator:")
        info(elevatorDone.formatFirstsAndSeconds())
        direction = 1
        clearElevator()
    }

    private val isEmpty: Boolean
        get() {
            for (i in 0 until requests) {
                if (elevatorRequests[i] != null) return true
            }
            return false
        }

    private fun upListTime(doneAtTime: Double): Double {
        var result = doneAtTime
        direction = 0
        for (i in index until requests) {
            if (!checkTimeHasCome(i)) continue
            result += computeDelayTime(result, i)
            break
        }
        return upList(result)
    }

    private fun downListTime(doneAtTime: Double): Double {
        var result = doneAtTime
        direction = 1
        for (i in index downTo 0) {
            if (checkTimeHasCome(i)) {
                result += computeDelayTime(result, i)
                break
            }
        }
        return downList(result)
    }

    private fun upList(doneAtTime: Double): Double {
        var result = doneAtTime
        direction = 0
        for (i in index until requests) {
            if (checkCompute(result, i)) {
                result = calculate(result, i)
                index = i
            }
        }
        return result
    }

    private fun downList(doneAtTime: Double): Double {
        var result = doneAtTime
        direction = 1
        for (i in index downTo 0) {
            if (checkCompute(result, i)) {
                result = calculate(result, i)
                index = i
            }
        }
        return result
    }

    private fun computeDelayTime(doneAtTime: Double, pos: Int): Double =
        elevatorRequests[pos]!![1] - doneAtTime

    private fun checkTimeHasCome(i: Int): Boolean =
        elevatorRequests[i] != null && elevatorRequests[i]!![1] == time[0]

    private fun checkCompute(doneAtTime: Double, i: Int): Boolean =
        elevatorRequests[i] != null && doneAtTime >= elevatorRequests[i]!![1]

    private fun checkTime(doneAtTime: Double): Boolean {
        for (doublesTime in elevatorRequests) {
            if (doublesTime != null && doneAtTime >= doublesTime[1]) return true
        }
        return false
    }

    private fun calculate(doneAtTime: Double, i: Int): Double {
        var result = doneAtTime

        val part1 = ROTATIONAL_DELAY + TRANSFER_DELAY
        val part2 = abs(elevatorRequests[i]!![0] - currentLocation) / CYLINDER_DIVISIONS
        val momentumDelay = if (elevatorRequests[i]!![0] != currentLocation) START_AND_STOP_DELAY else 0.0

        currentLocation = elevatorRequests[i]!![0]
        result += part1 + part2 + momentumDelay

        val section = ArrayList<Double>()
        time.remove(elevatorRequests[i]!![1])
        section.add(elevatorRequests[i]!![0])
        section.add(result)
        elevatorDone.add(section)
        elevatorRequests[i] = null
        return result
    }

    /** The FCFS Algorithm. It processes requests in the sequential order by which they arrive. */
    private fun runFcfsAlgorithm() {
        sort(fcfsRequests, 1)
        var doneAtTime = 0.0
        for (requestList in fcfsRequests) {
            if (doneAtTime < requestList!![1]) {
                doneAtTime += requestList[1] - doneAtTime
            }

            val part1 = ROTATIONAL_DELAY + TRANSFER_DELAY
            val part2 = abs(requestList[0] - currentLocation) / CYLINDER_DIVISIONS
            val momentumDelay = if (requestList[0] != currentLocation) START_AND_STOP_DELAY else 0.0

            currentLocation = requestList[0]
            doneAtTime += part1 + part2 + momentumDelay

            val section = buildList {
                add(requestList[0])
                add(doneAtTime)
            }
            fcfsDone.add(section)
        }

        fcfsAverage = average(fcfsDone)
        info("FCFS:")
        info(fcfsDone.formatFirstsAndSeconds())
        clearFcfs()
    }
}
