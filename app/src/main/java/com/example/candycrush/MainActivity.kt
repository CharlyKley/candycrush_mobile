package com.example.candycrush

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.candycrush.R.*
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {

        var candies = intArrayOf(
            R.drawable.bluecandy,
            R.drawable.greencandy,
            R.drawable.orangecandy,
            R.drawable.purplecandy,
            R.drawable.redcandy,
            R.drawable.yellowcandy
        )

        var widthOfBlock :Int = 0
        var noOFBlock :Int = 8
        var widthOfScreen :Int = 0
        lateinit var candy :ArrayList<ImageView>
        var CandyToBeDragged :Int = 0/*arrastrado*/
        var CandyToBeReplaced :Int = 0/*Reemplazado*/
        var notCandy :Int = R.drawable.transparent

        lateinit var mHandler: Handler
        private lateinit var scoreReult :TextView
        private lateinit var chronometer :Chronometer
        private lateinit var startNewGameBtn :Button

        var score = 0

    var interval = 100L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        startNewGameBtn=findViewById(R.id.startNewGameButton)
        startNewGameBtn.setOnClickListener {
            score = 0
            scoreReult.text = "$score"
            startNewGame()
        }

        chronometer = findViewById(R.id.chronometer)
        chronometer.setBase(SystemClock.elapsedRealtime())
        chronometer.start()
        scoreReult = findViewById(R.id.score)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        widthOfScreen = displayMetrics.heightPixels

        var heightOfScreen = displayMetrics.heightPixels

        widthOfBlock = widthOfScreen / noOFBlock

        candy = ArrayList()
        createBoard()
        for (imageView in candy){
            imageView.setOnTouchListener(
                object : OnSwipeListener(this){
                    override fun onSwipeRight() {
                        super.onSwipeRight()
                        CandyToBeDragged = imageView.id
                        CandyToBeReplaced = CandyToBeDragged +1
                        candyInterChacge()
                    }

                    override fun onSwipeLift() {
                        super.onSwipeLift()
                        CandyToBeDragged = imageView.id
                        CandyToBeReplaced = CandyToBeDragged -1
                        candyInterChacge()
                    }

                    override fun onSwipeTop() {
                        super.onSwipeTop()
                        CandyToBeDragged = imageView.id
                        CandyToBeReplaced = CandyToBeDragged - noOFBlock
                        candyInterChacge()
                    }

                    override fun onSwipeBottom() {
                        super.onSwipeBottom()
                        CandyToBeDragged = imageView.id
                        CandyToBeReplaced = CandyToBeDragged + noOFBlock
                        candyInterChacge()
                    }

                })
        }

        mHandler = Handler()
        startRepeat()

    }


    private fun candyInterChacge() {
        var background :Int = candy.get(CandyToBeReplaced).tag as Int
        var background1 :Int = candy.get(CandyToBeDragged).tag as Int

        candy.get(CandyToBeDragged).setImageResource(background)
        candy.get(CandyToBeReplaced).setImageResource(background1)

        candy.get(CandyToBeDragged).setTag(background)
        candy.get(CandyToBeReplaced).setTag(background1)

    }

    private fun checkRowForThree(){

        for(i in 0..61){
            var chosedCandy = candy.get(i).tag
            var isBlank :Boolean = candy.get(i).tag == notCandy
            val notValid = arrayOf(6,7,14,15,22,23,30,31,38,39,46,47,54,55)
            val list = asList(*notValid)
            if(!list.contains(i)){
                var x = i

                if(candy.get(x++).tag as Int == chosedCandy
                    && !isBlank
                    && candy.get(x++).tag as Int == chosedCandy
                    && candy.get(x).tag as Int == chosedCandy)
                {
                    score = score + 3
                    scoreReult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x--
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x--
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }

            }
        }
        moveDownCandies()
    }

    private fun checkColumnForThree(){
        for(i in 0..47){
            var chosedCandy = candy.get(i).tag
            var isBlank :Boolean = candy.get(i).tag == notCandy
            var x = i

                if(candy.get(x).tag as Int == chosedCandy
                    && !isBlank
                    && candy.get(x+noOFBlock).tag as Int == chosedCandy
                    && candy.get(x+2*noOFBlock).tag as Int == chosedCandy)
                {
                    score = score + 3
                    scoreReult.text = "$score"
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOFBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                    x = x + noOFBlock
                    candy.get(x).setImageResource(notCandy)
                    candy.get(x).setTag(notCandy)
                }


        }
        moveDownCandies()
    }

    private fun moveDownCandies() {
        val firstRow = arrayOf(1,2,3,4,5,6,7)
        val list = asList(*firstRow)
        for(i in 55 downTo 0){
            if(candy.get(i+noOFBlock).tag as Int == notCandy){
                candy.get(i+noOFBlock).setImageResource(candy.get(i).tag as Int)
                candy.get(i+noOFBlock).setTag(candy.get(i).tag as Int)
                candy.get(i).setImageResource(notCandy)
                candy.get(i).setTag(notCandy)
                if(list.contains(i) && candy.get(i).tag == notCandy){
                    var randomColor :Int = Math.abs(Math.random() * candies.size).toInt()
                    candy.get(i).setImageResource(candies[randomColor])
                    candy.get(i).setTag(candies[randomColor])
                }
            }
        }

        for(i in 0 ..7){
            if(candy.get(i).tag as Int == notCandy){

                var randomColor :Int = Math.abs(Math.random() * candies.size).toInt()
                candy.get(i).setImageResource(candies[randomColor])
                candy.get(i).setTag(candies[randomColor])
            }
        }
    }

    val repeatChecker :Runnable = object :Runnable{
        override fun run() {
            try {
                checkRowForThree()
                checkColumnForThree()
                moveDownCandies()
            }
            finally {
                mHandler.postDelayed(this,interval)
            }
        }

    }

    private fun startRepeat() {
        repeatChecker.run()
    }

    private fun createBoard() {
        val gridLayout = findViewById<GridLayout>(R.id.board)
        gridLayout.rowCount = noOFBlock
        gridLayout.columnCount = noOFBlock

        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height = widthOfScreen

        for(i in 0 until noOFBlock * noOFBlock){
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.
                view.ViewGroup.
                LayoutParams(widthOfBlock,widthOfBlock)
            imageView.maxHeight = widthOfBlock
            imageView.maxWidth = widthOfBlock

            var random :Int =Math.floor(Math.random() * candies.size).toInt()

            // randomInde
            imageView.setImageResource(candies[random])
            imageView.setTag(candies[random])

            candy.add(imageView)
            gridLayout.addView(imageView)
        }
    }

        private fun startNewGame() {
            // Reiniciar cronómetro
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()

            // Reiniciar puntuación
            score = 0
            scoreReult.text = "$score"

            // Reiniciar el tablero de dulces
            resetBoard()

            // Comenzar el juego
            startRepeat()
        }

    private fun resetBoard() {
        for (imageView in candy) {
            val randomCandy = candies.random()
            imageView.setImageResource(randomCandy)
            imageView.tag = randomCandy
        }
    }


}