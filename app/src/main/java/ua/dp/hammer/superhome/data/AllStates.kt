package ua.dp.hammer.superhome.data

data class AllStates(val projectorsState: List<ProjectorState>?,
                     val fanState: FanState,
                     val alarmsState: AlarmsState,
                     val shuttersState: List<ShutterState>?)