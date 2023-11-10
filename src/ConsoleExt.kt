/*
 * ConsoleExt.kt is licensed under the MIT License.
 *
 * Copyright (c) 2023 Ali Khaleqi Yekta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import Color.RESET
import Color.BLACK
import Color.RED
import Color.GREEN
import Color.YELLOW
import Color.BLUE
import Color.PURPLE
import Color.CYAN
import Color.WHITE
import Color.BLACK_BRIGHT
import Color.RED_BRIGHT
import Color.GREEN_BRIGHT
import Color.YELLOW_BRIGHT
import Color.BLUE_BRIGHT
import Color.PURPLE_BRIGHT
import Color.CYAN_BRIGHT
import Color.WHITE_BRIGHT
import Color.BLACK_BOLD
import Color.RED_BOLD
import Color.GREEN_BOLD
import Color.YELLOW_BOLD
import Color.BLUE_BOLD
import Color.PURPLE_BOLD
import Color.CYAN_BOLD
import Color.WHITE_BOLD
import Color.BLACK_BOLD_BRIGHT
import Color.RED_BOLD_BRIGHT
import Color.GREEN_BOLD_BRIGHT
import Color.YELLOW_BOLD_BRIGHT
import Color.BLUE_BOLD_BRIGHT
import Color.PURPLE_BOLD_BRIGHT
import Color.CYAN_BOLD_BRIGHT
import Color.WHITE_BOLD_BRIGHT
import Color.BLACK_UNDERLINED
import Color.RED_UNDERLINED
import Color.GREEN_UNDERLINED
import Color.YELLOW_UNDERLINED
import Color.BLUE_UNDERLINED
import Color.PURPLE_UNDERLINED
import Color.CYAN_UNDERLINED
import Color.WHITE_UNDERLINED
import Color.BG_BLACK
import Color.BG_RED
import Color.BG_GREEN
import Color.BG_YELLOW
import Color.BG_BLUE
import Color.BG_PURPLE
import Color.BG_CYAN
import Color.BG_WHITE
import Color.BG_BLACK_BRIGHT
import Color.BG_RED_BRIGHT
import Color.BG_GREEN_BRIGHT
import Color.BG_YELLOW_BRIGHT
import Color.BG_BLUE_BRIGHT
import Color.BG_PURPLE_BRIGHT
import Color.BG_CYAN_BRIGHT
import Color.BG_WHITE_BRIGHT
import Print.lineLeft
import Print.lineRight

// Read ================================================================================================================

fun readString(): String = runCatching(::readlnOrNull)
    .onFailure { error("Error while reading the input!") }
    .getOrNull() ?: readString()

fun readInt(): Int = readString().toIntOrNull() ?: run {
    error("Input mismatch! Enter a number")
    readInt()
}

fun readDouble(): Double = readString().toDoubleOrNull() ?: run {
    error("Input mismatch! Enter a number")
    readDouble()
}

object Ask {
    private fun askMessage(message: String) = print(message.cyanBoldBright().reset() + " ")

    fun int(message: String): Int {
        askMessage(message)
        return readInt()
    }

    fun double(message: String): Double {
        askMessage(message)
        return readDouble()
    }
}

// Print ===============================================================================================================

private object Print {
    fun line(first: String, second: String, size: Int = 20) =
        (first.purpleBoldBright() + second.cyanBoldBright()).repeat(size).reset()

    val lineLeft = line("-", "=")
    val lineRight = line("=", "-")
}

fun title(text: String) = println(lineLeft + ' ' + text.yellowUnderlined().yellowBoldBright().reset() + ' ' + lineRight)
fun subTitle(text: String) = println("-=-=-=-=-".white() + "> ".yellowBoldBright().reset() + text)
fun info(vararg texts: String) {
    var i = 0
    val coloredInfo = texts.joinToString(prefix = "", postfix = "") {
        when (i++ % 2 == 0) {
            true -> it.greenBoldBright().reset()
            false -> it.greenBold().reset()
        }
    }
    println(coloredInfo)
}

fun line(extraLen: Int = 0) = println(
    Print.line("-", "=", size = 21 + 21 + extraLen / 2).let {
        when (extraLen % 2 == 1) {
            true -> it.replaceFirst("-".purpleBoldBright() + "=".cyanBoldBright(), "=".cyanBoldBright())
            false -> it
        }
    }
)

fun line(extraLenFrom: String) = line(extraLen = extraLenFrom.length)

fun section(title: String, content: () -> Unit) {
    title(title)
    content()
    line(extraLenFrom = title)
}

fun option(code: String, text: String) = println(code.greenBoldBright() + "> ".green().reset() + text)

fun error(text: String) = println(
    "[".yellowBold() + "Error".redBoldBright() + "]".yellowBold()
            + ": ".whiteBoldBright()
            + text.reset()
)

// Console Color =======================================================================================================

private object Color {
    const val RESET = "\u001b[0m"

    // Regular Colors
    const val BLACK = "\u001b[0;30m"
    const val RED = "\u001b[0;31m"
    const val GREEN = "\u001b[0;32m"
    const val YELLOW = "\u001b[0;33m"
    const val BLUE = "\u001b[0;34m"
    const val PURPLE = "\u001b[0;35m"
    const val CYAN = "\u001b[0;36m"
    const val WHITE = "\u001b[0;37m"
    const val BLACK_BRIGHT = "\u001b[0;90m"
    const val RED_BRIGHT = "\u001b[0;91m"
    const val GREEN_BRIGHT = "\u001b[0;92m"
    const val YELLOW_BRIGHT = "\u001b[0;93m"
    const val BLUE_BRIGHT = "\u001b[0;94m"
    const val PURPLE_BRIGHT = "\u001b[0;95m"
    const val CYAN_BRIGHT = "\u001b[0;96m"
    const val WHITE_BRIGHT = "\u001b[0;97m"

    // Bold
    const val BLACK_BOLD = "\u001b[1;30m"
    const val RED_BOLD = "\u001b[1;31m"
    const val GREEN_BOLD = "\u001b[1;32m"
    const val YELLOW_BOLD = "\u001b[1;33m"
    const val BLUE_BOLD = "\u001b[1;34m"
    const val PURPLE_BOLD = "\u001b[1;35m"
    const val CYAN_BOLD = "\u001b[1;36m"
    const val WHITE_BOLD = "\u001b[1;37m"
    const val BLACK_BOLD_BRIGHT = "\u001b[1;90m"
    const val RED_BOLD_BRIGHT = "\u001b[1;91m"
    const val GREEN_BOLD_BRIGHT = "\u001b[1;92m"
    const val YELLOW_BOLD_BRIGHT = "\u001b[1;93m"
    const val BLUE_BOLD_BRIGHT = "\u001b[1;94m"
    const val PURPLE_BOLD_BRIGHT = "\u001b[1;95m"
    const val CYAN_BOLD_BRIGHT = "\u001b[1;96m"
    const val WHITE_BOLD_BRIGHT = "\u001b[1;97m"

    // Underline
    const val BLACK_UNDERLINED = "\u001b[4;30m"
    const val RED_UNDERLINED = "\u001b[4;31m"
    const val GREEN_UNDERLINED = "\u001b[4;32m"
    const val YELLOW_UNDERLINED = "\u001b[4;33m"
    const val BLUE_UNDERLINED = "\u001b[4;34m"
    const val PURPLE_UNDERLINED = "\u001b[4;35m"
    const val CYAN_UNDERLINED = "\u001b[4;36m"
    const val WHITE_UNDERLINED = "\u001b[4;37m"

    // Background
    const val BG_BLACK = "\u001b[40m"
    const val BG_RED = "\u001b[41m"
    const val BG_GREEN = "\u001b[42m"
    const val BG_YELLOW = "\u001b[43m"
    const val BG_BLUE = "\u001b[44m"
    const val BG_PURPLE = "\u001b[45m"
    const val BG_CYAN = "\u001b[46m"
    const val BG_WHITE = "\u001b[47m"
    const val BG_BLACK_BRIGHT = "\u001b[0;100m"
    const val BG_RED_BRIGHT = "\u001b[0;101m"
    const val BG_GREEN_BRIGHT = "\u001b[0;102m"
    const val BG_YELLOW_BRIGHT = "\u001b[0;103m"
    const val BG_BLUE_BRIGHT = "\u001b[0;104m"
    const val BG_PURPLE_BRIGHT = "\u001b[0;105m"
    const val BG_CYAN_BRIGHT = "\u001b[0;106m"
    const val BG_WHITE_BRIGHT = "\u001b[0;107m"
}

fun String.reset() = this + RESET

fun String.black() = BLACK + this
fun String.red() = RED + this
fun String.green() = GREEN + this
fun String.yellow() = YELLOW + this
fun String.blue() = BLUE + this
fun String.purple() = PURPLE + this
fun String.cyan() = CYAN + this
fun String.white() = WHITE + this
fun String.blackBright() = BLACK_BRIGHT + this
fun String.redBright() = RED_BRIGHT + this
fun String.greenBright() = GREEN_BRIGHT + this
fun String.yellowBright() = YELLOW_BRIGHT + this
fun String.blueBright() = BLUE_BRIGHT + this
fun String.purpleBright() = PURPLE_BRIGHT + this
fun String.cyanBright() = CYAN_BRIGHT + this
fun String.whiteBright() = WHITE_BRIGHT + this
fun String.blackBold() = BLACK_BOLD + this
fun String.redBold() = RED_BOLD + this
fun String.greenBold() = GREEN_BOLD + this
fun String.yellowBold() = YELLOW_BOLD + this
fun String.blueBold() = BLUE_BOLD + this
fun String.purpleBold() = PURPLE_BOLD + this
fun String.cyanBold() = CYAN_BOLD + this
fun String.whiteBold() = WHITE_BOLD + this
fun String.blackBoldBright() = BLACK_BOLD_BRIGHT + this
fun String.redBoldBright() = RED_BOLD_BRIGHT + this
fun String.greenBoldBright() = GREEN_BOLD_BRIGHT + this
fun String.yellowBoldBright() = YELLOW_BOLD_BRIGHT + this
fun String.blueBoldBright() = BLUE_BOLD_BRIGHT + this
fun String.purpleBoldBright() = PURPLE_BOLD_BRIGHT + this
fun String.cyanBoldBright() = CYAN_BOLD_BRIGHT + this
fun String.whiteBoldBright() = WHITE_BOLD_BRIGHT + this

fun String.blackUnderlined() = BLACK_UNDERLINED + this
fun String.redUnderlined() = RED_UNDERLINED + this
fun String.greenUnderlined() = GREEN_UNDERLINED + this
fun String.yellowUnderlined() = YELLOW_UNDERLINED + this
fun String.blueUnderlined() = BLUE_UNDERLINED + this
fun String.purpleUnderlined() = PURPLE_UNDERLINED + this
fun String.cyanUnderlined() = CYAN_UNDERLINED + this
fun String.whiteUnderlined() = WHITE_UNDERLINED + this

fun String.bgBlack() = BG_BLACK + this
fun String.bgRed() = BG_RED + this
fun String.bgGreen() = BG_GREEN + this
fun String.bgYellow() = BG_YELLOW + this
fun String.bgBlue() = BG_BLUE + this
fun String.bgPurple() = BG_PURPLE + this
fun String.bgCyan() = BG_CYAN + this
fun String.bgWhite() = BG_WHITE + this
fun String.bgBlackBright() = BG_BLACK_BRIGHT + this
fun String.bgRedBright() = BG_RED_BRIGHT + this
fun String.bgGreenBright() = BG_GREEN_BRIGHT + this
fun String.bgYellowBright() = BG_YELLOW_BRIGHT + this
fun String.bgBlueBright() = BG_BLUE_BRIGHT + this
fun String.bgPurpleBright() = BG_PURPLE_BRIGHT + this
fun String.bgCyanBright() = BG_CYAN_BRIGHT + this
fun String.bgWhiteBright() = BG_WHITE_BRIGHT + this
