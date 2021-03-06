      @                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        y
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.ProjectMain.FishingApp

val REQUEST_PERMISSIONS_REQUEST_CODE = 34

fun checkLocationPermissions(activity: Activity) : Boolean {
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        return true
    }
    else {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
        return false
    }
}

fun isPermissionGranted(code: Int, grantResults: IntArray): Boolean {
    var permissionGranted = false;
    if (code == REQUEST_PERMISSIONS_REQUEST_CODE) {
        when {
            grantResults.isEmpty() -> Log.i("Location", "User interaction was cancelled.")
            (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                permissionGranted = true
                Log.i("Location", "Permission Granted.")
            }
            else -> Log.i("Location", "Permission Denied.")
        }
    }
    return permissionGranted
}

@SuppressLint("MissingPermission")
fun setCurrentLocation(app: FishingApp) {
    app.locationClient.lastLocation
        .addOnSuccessListener { location : Location? ->
            app.currentLocation = location!!
        }
}

@SuppressLint("RestrictedApi")
fun createDefaultLocationRequest() : LocationRequest {
    val locationRequest = LocationRequest().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    return locationRequest
}

@SuppressLint("MissingPermission")
fun trackLocation(app: FishingApp) {
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                app.currentLocation = locationResult.locations.last()
                app.marker.position = LatLng(app.currentLocation.latitude,
                    app.currentLocation.longitude)
            }
        }
    }

    app.locationClient.requestLocationUpdates(createDefaultLocationRequest(),
        locationCallback, null)
}

fun setMapMarker(app: FishingApp) {

    val pos = LatLng(app.currentLocation.latitude,
        app.currentLocation.longitude)

    app.marker = app.mMap.addMarker(
        MarkerOptions().position(pos)
        .title("My Current Location")
        .snippet("This is Me!")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

    app.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f))
}

