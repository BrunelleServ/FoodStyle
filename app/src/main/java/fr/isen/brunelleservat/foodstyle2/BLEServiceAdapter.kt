package fr.isen.brunelleservat.foodstyle2

import android.app.AlertDialog
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.brunelleservat.foodstyle2.model.BLEService

class BLEServiceAdapter( private val gatt: BluetoothGatt?, private val serviceList: MutableList<BLEService>, private val context: Context):
        ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharacteristicViewHolder>
        (serviceList){
            class ServiceViewHolder(itemView: View): GroupViewHolder(itemView){
                val serviceName = itemView.findViewById<TextView>(R.id.serviceName)
                val serviceArrow = itemView.findViewById<TextView>(R.id.uuidAcces)

                override fun expand() {
                    super.expand()
                    serviceArrow.animate().rotation(-180f).setDuration(400L).start()
                }
                override fun collapse() {
                    super.collapse()
                    serviceArrow.animate().rotation(0f).setDuration(400L).start()
                }
            }
            class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView){
                val characteristicName = itemView.findViewById<TextView>(R.id.characName)
                val characteristicRead = itemView.findViewById<TextView>(R.id.characRead)
                val characteristicWrite = itemView.findViewById<TextView>(R.id.characWrite)
                val characteristicNotify = itemView.findViewById<TextView>(R.id.characNotify)
                val characteristicProp: TextView = itemView.findViewById(R.id.propView)
                val characteristicAcces: TextView = itemView.findViewById(R.id.accesName)
                val characteristicValue: TextView = itemView.findViewById(R.id.valueText)


            }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_b_l_e_service_cell, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): CharacteristicViewHolder = CharacteristicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_b_l_e_characteristic_cell, parent, false)
    )

    override fun onBindChildViewHolder(holder: CharacteristicViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        holder.characteristicName.text = (group.items[childIndex] as BluetoothGattCharacteristic).uuid.toString()
        val characteristicAcces: BluetoothGattCharacteristic? = (group as BLEService).items[childIndex]
        val uuid = "UUID : ${characteristicAcces?.uuid}"

        holder.characteristicAcces.text = uuid
        holder.characteristicRead.visibility = View.GONE
        holder.characteristicWrite.visibility = View.GONE
        holder.characteristicNotify.visibility = View.GONE
        if (characteristicAcces != null) {
            holder.characteristicProp.text = "proprietes : ${propBLE(characteristicAcces.properties)}"
        }
        holder.characteristicWrite.setOnClickListener {
            val editView = View.inflate(context, R.layout.activity_write, null)
            val dialogue = AlertDialog.Builder(context)
            dialogue.setView(editView)
            dialogue.setPositiveButton("VALIDER") { _, _ ->
                //val texte = editView.popup.text.toString().toByteArray()
                val popup: TextView = editView.findViewById(R.id.editNom)
                val texte = popup.text.toString().toByteArray()
                if (characteristicAcces != null) {
                    characteristicAcces.setValue(texte)
                }
                val result1 = gatt?.writeCharacteristic(characteristicAcces)

                val result = gatt?.readCharacteristic(characteristicAcces)
            }
            dialogue.setNegativeButton("ANNULER") {
                dialogue, _ -> dialogue.cancel() }
            dialogue.create()
            dialogue.show()
        }

        if (characteristicAcces != null) {
            if (propBLE(characteristicAcces.properties).contains("Lire")) {
                holder.characteristicRead.visibility = View.VISIBLE
            }
        }
        if (characteristicAcces != null) {
            if (propBLE(characteristicAcces.properties).contains("Ecrire")) {
                holder.characteristicWrite.visibility = View.VISIBLE
            }
        }
        if (characteristicAcces != null) {
            if (propBLE(characteristicAcces.properties).contains("Notifier")) {
                holder.characteristicNotify.visibility = View.VISIBLE
            }
        }

        holder.characteristicRead.setOnClickListener {
            val result = gatt?.readCharacteristic(characteristicAcces)

            if (characteristicAcces != null) {
                if (characteristicAcces.value != null) {

                    val textRecus = characteristicAcces?.let { itt -> String(itt.value) }
                    holder.characteristicValue.text = "Valeur : ${ textRecus}"
                }
            }

        }
        var enable : Boolean = false
        if (characteristicAcces != null) {
            if (characteristicAcces.uuid.toString() == BLEAcces.getBLEAttributeFromUUID(
                            characteristicAcces.uuid.toString()).acces && enable) {
                if (characteristicAcces.value == null)
                    holder.characteristicValue.text = "Valeur: 0"
                else {
                    if (characteristicAcces != null) {
                        holder.characteristicValue.text =
                                "Valeur : ${boardArray(characteristicAcces.value)}"
                    }
                }

            }
        }
        var enabled : Boolean = false
        holder.characteristicNotify.setOnClickListener {
            if (!enabled) {
                enabled = true
                gatt?.setCharacteristicNotification(characteristicAcces, true)

                if (characteristicAcces != null) {
                    if (characteristicAcces.descriptors.size > 0) {

                        val descriptors = characteristicAcces?.descriptors
                        if (descriptors != null) {
                            for (descriptor in descriptors) {

                                if (characteristicAcces.properties and
                                        BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                                    descriptor.value =
                                            if (enabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                                } else if (characteristicAcces != null) {
                                    if (characteristicAcces.properties and
                                            BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                                        descriptor.value =
                                                if (enabled) BluetoothGattDescriptor.ENABLE_INDICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                                    }
                                }
                                gatt?.writeDescriptor(descriptor)
                            }
                        }

                    }
                }
            } else {
                enabled = false
                gatt?.setCharacteristicNotification(characteristicAcces, false)
            }
        }


    }

    private fun propBLE(property: Int): StringBuilder {
        val s = StringBuilder()

        if (property and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
            s.append("Ecrire") }
        if (property and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
            s.append("Lire") }
        if (property and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
            s.append("Notifier") }
        if (s.isEmpty()) s.append("Aucun")

        return s
    }

    private fun boardArray(array: ByteArray): String {
        val but = StringBuilder(array.size*2)
        for ( byte in array ) {
            val toAppend = String.format("%X", byte)
            but.append(toAppend).append("-")
        }
        but.setLength(but.length - 1)
        return but.toString()
    }



    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>
    ) {
        val title = BLEAcces.getBLEAttributeFromUUID(group.title).title
        holder.serviceName.text = title
        holder.serviceArrow.text = group.title
    }



    enum class BLEAcces(val acces: String, val title: String) {
        ACCES_GENERIQUE("00001800-0000-1000-8000-00805f9b34fb", "Accès générique"),
        ATTRIBUT_GENERIQUE("00001801-0000-1000-8000-00805f9b34fb", "Attribut générique"),
        SERVICE_SPECIFIQUE("466c1234-f593-11e8-8eb2-f2801f1b9fd1", "Service Spécifique "),
        SERVICE_SPE2("466c9abc-f593-11e8-8eb2-f2801f1b9fd1", "Service Spécifique 2"),
        UNKNOW_SERVICE("", "Inconnu");

    companion object {
        fun getBLEAttributeFromUUID(uuid: String) = values().firstOrNull {
            it.acces == uuid
        } ?: UNKNOW_SERVICE
    }
}

}