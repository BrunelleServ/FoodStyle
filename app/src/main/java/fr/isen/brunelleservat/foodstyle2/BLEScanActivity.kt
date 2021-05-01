package fr.isen.brunelleservat.foodstyle2

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import fr.isen.brunelleservat.foodstyle2.databinding.ActivityBLEScanBinding


class BLEScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBLEScanBinding
    private var isScanning =false
    private val bluetoothAdapter: BluetoothAdapter? =null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBLEScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothAdapter= getSystemService(BluetoothManager::class.java)?.adapter
        startBLEIfPossible()


        binding.PlayPauseView.setOnClickListener {
            togglePlaypauseAction()
        }
        binding.BLEScanTitle.setOnClickListener {
            togglePlaypauseAction()
        }

    }

    private fun startBLEIfPossible() {
        when {
            !isDeviceHasBLESupport() && bluetoothAdapter == null -> {
                Toast.makeText(
                        this,
                        "Cet appareil n'est pas compatible avec le module BLE",
                        Toast.LENGTH_SHORT
                ).show()
            }
            !(bluetoothAdapter?.isEnabled ?: false) -> {
                //je dois activer le bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            }
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
            }
            else -> {
                //on peut faire du BLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            startBLEIfPossible()

        }
    }

    private fun isDeviceHasBLESupport(): Boolean =
            packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

    private fun togglePlaypauseAction(){
        isScanning = !isScanning
        if(isScanning){
            binding.BLEScanTitle.text = getString(R.string.ble_scan_pause_title)
            binding.PlayPauseView.setImageResource(R.drawable.ic_pause)
            binding.progressBar.isVisible = true
            binding.titleDividerNoCustom.isVisible = false

        } else{
            binding.BLEScanTitle.text = getString(R.string.ble_scan_play_title)
            binding.PlayPauseView.setImageResource(R.drawable.ic_play)
            binding.progressBar.visibility = View.INVISIBLE
            binding.titleDividerNoCustom.isVisible = true

        }
    }
    companion object{
        const private val REQUEST_ENABLE_BT= 33
        const private val REQUEST_PERMISSION_LOCATION = 33
    }
}