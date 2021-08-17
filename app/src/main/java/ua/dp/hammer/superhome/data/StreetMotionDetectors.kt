package ua.dp.hammer.superhome.data

data class StreetMotionDetectors(
    val entrance: MotionDetector,
    val entranceYard: MotionDetector,
    val porch: MotionDetector,
    val backYard: MotionDetector)
