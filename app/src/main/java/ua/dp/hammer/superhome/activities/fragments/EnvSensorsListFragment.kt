package ua.dp.hammer.superhome.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ua.dp.hammer.superhome.databinding.FragmentEnvSensorsListBinding
import ua.dp.hammer.superhome.models.SensorViewModel

class EnvSensorsListFragment : Fragment() {
    private lateinit var vm: SensorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(this).get(SensorViewModel::class.java)

        /*Intent(activity, TemperaturesService::class.java).also { intent ->
            activity?.startService(intent)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        val binding = FragmentEnvSensorsListBinding.inflate(inflater, container, false)

        //binding.envSensorsList.adapter  =

        //vm.getSensorsInfo().observe(viewLifecycleOwner, Observer {})
        return binding.root
    }
}