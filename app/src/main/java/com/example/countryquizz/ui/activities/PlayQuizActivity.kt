package com.example.countryquizz.ui.activities

import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.countryquizz.R
import com.example.countryquizz.databinding.ActivityPlayQuizBinding
import com.example.countryquizz.ui.viewmodels.PlayQuizViewModel
import com.example.countryquizz.ui.viewmodels.SeeResultsViewModel
import com.example.countryquizz.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayQuizActivity : AppCompatActivity() {

    private val playQuizViewModel by viewModel<PlayQuizViewModel>()
    private val seeResultsViewModel by viewModel<SeeResultsViewModel>()
    private lateinit var binding: ActivityPlayQuizBinding

    private lateinit var mSoundPool: SoundPool
    private var mLoaded: Boolean = false
    var mSoundMap: HashMap<Int, Int> = HashMap()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayQuizBinding.inflate(layoutInflater).also {
            val binding = it
            it.progressBarHorizontal.progress = 0

            val firstButton = it.btnFirstAnswer
            it.btnFirstAnswer.setOnClickListener {
                playQuizViewModel.isCorrect(firstButton.text.toString())
                binding.progressBarHorizontal.progress++
            }

            val secondButton = it.btnSecondAnswer
            it.btnSecondAnswer.setOnClickListener {
                playQuizViewModel.isCorrect(secondButton.text.toString())
                binding.progressBarHorizontal.progress++
            }

            val thirdButton = it.btnThirdAnswer
            it.btnThirdAnswer.setOnClickListener {
                playQuizViewModel.isCorrect(thirdButton.text.toString())
                binding.progressBarHorizontal.progress++
            }

            val fourthButton = it.btnFourthAnswer
            it.btnFourthAnswer.setOnClickListener {
                playQuizViewModel.isCorrect(fourthButton.text.toString())
                binding.progressBarHorizontal.progress++
            }
        }

        playQuizViewModel.getAllCountries()

        playQuizViewModel.didFetchCountries.observe(this, {
            binding.btnFirstAnswer.text = playQuizViewModel.getRandomCountryName()
            binding.btnSecondAnswer.text = playQuizViewModel.getRandomCountryName()
            binding.btnThirdAnswer.text = playQuizViewModel.getRandomCountryName()
            binding.btnFourthAnswer.text = playQuizViewModel.getRandomCountryName()

            Glide.with(this)
                .load(playQuizViewModel.getRandomCountryImage())
                .into(binding.ivFlag)
        })

        playQuizViewModel.currentQuestionFlag.observe(this, {
            binding.btnFirstAnswer.text = playQuizViewModel.getRandomCountryName()
            binding.btnSecondAnswer.text = playQuizViewModel.getRandomCountryName()
            binding.btnThirdAnswer.text = playQuizViewModel.getRandomCountryName()
            binding.btnFourthAnswer.text = playQuizViewModel.getRandomCountryName()

            Glide.with(this)
                .load(playQuizViewModel.getRandomCountryImage())
                .into(binding.ivFlag)
        })

        playQuizViewModel.allQuestionsAnswered.observe(this, {
            CoroutineScope(Dispatchers.Default).launch {
                seeResultsViewModel.addToDatabase(playQuizViewModel.getCorrectAnswers().toString())
            }
        })

        seeResultsViewModel.didUploadResult.observe(this, {
            finish()
        })

        loadSounds()

        playQuizViewModel.isAnswerCorrect.observe(this, {
            if(it){
                playSound(R.raw.correct)
            }
            else {
                playSound(R.raw.wrong)
            }
        })

        setContentView(binding.root)
    }

    private fun loadSounds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mSoundPool = SoundPool.Builder().setMaxStreams(10).build()
        }
        else {
            this.mSoundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        }
        this.mSoundPool.setOnLoadCompleteListener { _, _, _ -> mLoaded = true }
        this.mSoundMap[R.raw.correct] = this.mSoundPool.load(this, R.raw.correct, 1)
        this.mSoundMap[R.raw.wrong] = this.mSoundPool.load(this, R.raw.wrong, 1)
    }

    private fun playSound(selectedSound: Int) {
        val soundID = this.mSoundMap[selectedSound] ?: 0
        this.mSoundPool.play(soundID, 1f, 1f, 1, 0, 1f)
    }

}