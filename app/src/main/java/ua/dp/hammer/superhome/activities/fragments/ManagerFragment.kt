package ua.dp.hammer.superhome.activities.fragments

import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.dp.hammer.superhome.R
import ua.dp.hammer.superhome.data.ShutterState
import ua.dp.hammer.superhome.data.ShutterStates
import ua.dp.hammer.superhome.databinding.FragmentManagerBinding
import ua.dp.hammer.superhome.models.ManagerViewModel
import ua.dp.hammer.superhome.repositories.settings.LocalSettingsRepository


class ManagerFragment : Fragment() {
    private val viewModel: ManagerViewModel by viewModels {
        object : ViewModelProvider.NewInstanceFactory() {
            val currentContext = context ?: throw IllegalStateException("Context cannot be null")

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>) =
                ManagerViewModel(LocalSettingsRepository.getInstance(currentContext)) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: created")
    }

    override fun onStart() {
        super.onStart()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: started")

        //val binding = FragmentManagerBinding.inflate(layoutInflater)


    }

    override fun onResume() {
        super.onResume()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: resumed")
    }

    override fun onPause() {
        super.onPause()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: paused")
    }

    override fun onStop() {
        super.onStop()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: stopped")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentManagerBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.projectorsButton.setOnLongClickListener() {
            viewModel.onProjectorsLongButtonClick(it as ImageButton)
            true
        }

        binding.cameraRecordingButton.setOnLongClickListener() {
            val dialog = CameraRecordingSettingsDialog()

            dialog.show(this.parentFragmentManager, "camera_settings")

            true
        }

        binding.fanButton.setOnLongClickListener {
            viewModel.onFanLongButtonClick(it as ImageButton)
            true
        }

        viewModel.projectorsButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.projectorsButton.isSelected = isSelected
        })

        viewModel.cameraButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.cameraRecordingButton.isSelected = isSelected
        })

        viewModel.fanButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.fanButton.isSelected = isSelected
        })

        // For button disabling while fun is already turned on
        viewModel.fanButtonEnabled.observe(viewLifecycleOwner, Observer<Boolean> { isEnabled ->
            binding.fanButton.isEnabled = isEnabled
        })

        viewModel.roomShutterButtonState.observe(viewLifecycleOwner, Observer<ShutterState> { state ->
            changeShutterStateButton(binding.roomShutterButton, state)
        })

        viewModel.kitchen1ShutterButtonState.observe(viewLifecycleOwner, Observer<ShutterState> { state ->
            changeShutterStateButton(binding.kitchenShutter1Button, state)
        })

        viewModel.kitchen2ShutterButtonState.observe(viewLifecycleOwner, Observer<ShutterState> { state ->
            changeShutterStateButton(binding.kitchenShutter2Button, state)
        })

        return binding.root
    }

    private fun changeShutterStateButton(button: ImageButton, state: ShutterState) {
        val currentContext = context ?: throw java.lang.IllegalStateException()

        if (state.state == ShutterStates.SHUTTER_CLOSED || state.state == ShutterStates.SHUTTER_OPENED) {
            button.isClickable = true

            if (state.state == ShutterStates.SHUTTER_CLOSED) {
                if (state.notAvailable) {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_closed_disabled))
                } else {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_closed))
                }
            } else {
                if (state.notAvailable) {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_opened_disabled))
                } else {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_opened))
                }
            }
        } else if (state.state == ShutterStates.SHUTTER_CLOSING || state.state == ShutterStates.SHUTTER_OPENING) {
            button.isClickable = false

            if (state.state == ShutterStates.SHUTTER_CLOSING) {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_closing))
            } else {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_opening))
            }

            val animation = button.drawable as AnimationDrawable
            animation.stop()
            animation.selectDrawable(0)
            animation.start()
        }
    }
}