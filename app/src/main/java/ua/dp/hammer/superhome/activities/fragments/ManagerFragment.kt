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
import ua.dp.hammer.superhome.data.FanState
import ua.dp.hammer.superhome.data.ProjectorState
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.resumeMonitoring()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: resumed")
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopMonitoring()
        Log.d(null, "~~~ " + ManagerFragment::class.java.simpleName + " state: stopped")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        val binding = FragmentManagerBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.root.setOnTouchListener {v, event ->
            true
        }

        binding.projectorsButton.setOnLongClickListener() {
            viewModel.onProjectorsLongButtonClick(it as ImageButton)
            true
        }

        viewModel.projectorsButtonState.observe(viewLifecycleOwner, Observer<ProjectorState> { state ->
            changeProjectorStateButton(binding.projectorsButton, state)
        })

        viewModel.fanButtonState.observe(viewLifecycleOwner, Observer<FanState> { state ->
            changeFanStateButton(binding.fanButton, state)
        })

        binding.cameraRecordingButton.setOnLongClickListener {
            val dialog = CameraRecordingSettingsDialog()

            dialog.show(this.parentFragmentManager, "camera_settings")

            true
        }

        binding.fanButton.setOnLongClickListener {
            val deviceName: String? = viewModel.fanButtonState.value?.deviceName

            if (deviceName != null) {
                val dialog = FanSettingsDialog(deviceName)
                dialog.show(this.parentFragmentManager, "fan_settings")
            }

            true
        }

        viewModel.cameraButtonSelected.observe(viewLifecycleOwner, Observer<Boolean> { isSelected ->
            binding.cameraRecordingButton.isSelected = isSelected
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
                if (state.notAvailable == false) {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_closed))
                } else {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_closed_disabled))
                }
            } else {
                if (state.notAvailable == false) {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_opened))
                } else {
                    button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.shutter_opened_disabled))
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

    private fun changeProjectorStateButton(button: ImageButton, state: ProjectorState) {
        val currentContext = context ?: throw java.lang.IllegalStateException()

        if (state.turnedOn) {
            if (state.notAvailable == false) {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.lamp_turned_on))
            } else {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.lamp_turned_on_disabled))
            }
        } else {
            if (state.notAvailable == false) {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.lamp_turned_off))
            } else {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.lamp_turned_off_disabled))
            }
        }
    }

    private fun changeFanStateButton(button: ImageButton, state: FanState) {
        val currentContext = context ?: throw java.lang.IllegalStateException()

        if (state.turnedOn == true) {
            if (state.notAvailable == false) {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.fan_turned_on))
            } else {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.fan_turned_on_disabled))
            }
        } else {
            if (state.notAvailable == false) {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.fan_turned_off))
            } else {
                button.setImageDrawable(ContextCompat.getDrawable(currentContext, R.drawable.fan_turned_off_disabled))
            }
        }
    }
}