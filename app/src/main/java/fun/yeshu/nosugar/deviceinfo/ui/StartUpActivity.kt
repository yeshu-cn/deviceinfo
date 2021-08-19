package `fun`.yeshu.nosugar.deviceinfo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `fun`.yeshu.nosugar.deviceinfo.databinding.ActivityStartUpBinding
import `fun`.yeshu.nosugar.deviceinfo.utils.SpUtils
import android.content.Intent

class StartUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val caseId = SpUtils.getCaseId(this)
        if (caseId.isEmpty()) {
            startActivity(Intent(this, SetInfoActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}