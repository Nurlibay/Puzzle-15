package uz.unidev.numberpuzzle15.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import uz.unidev.numberpuzzle15.R
import uz.unidev.numberpuzzle15.databinding.DialogWinBinding

class WinDialog(private val moves: Int, private val time: String) :
    DialogFragment(R.layout.dialog_win) {

    private lateinit var binding: DialogWinBinding

    private var nextButtonClick: ((Unit) -> Unit?)? = null
    fun nextButtonClickListener(block: ((Unit) -> Unit?)?) {
        nextButtonClick = block
    }

    private var homeButtonClick: ((Unit) -> Unit?)? = null
    fun homeButtonClickListener(block: ((Unit) -> Unit?)?) {
        homeButtonClick = block
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogWinBinding.bind(view).apply {
            requireDialog().window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requireDialog().setCancelable(false)
            val tvMoves = tvScore
            val tvTime = tvTime
            tvMoves.text = moves.toString()
            tvTime.text = time
            requireDialog().show()

            btnNext.setOnClickListener {
                nextButtonClick?.invoke(Unit)
                dialog?.dismiss()
            }

            btnHome.setOnClickListener {
                homeButtonClick?.invoke(Unit)
            }
        }
    }
}