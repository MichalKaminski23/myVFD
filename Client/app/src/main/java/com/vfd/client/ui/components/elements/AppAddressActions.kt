package com.vfd.client.ui.components.elements

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.ui.components.texts.AppTextField

@Composable
fun AppAddressActions(
    address: AddressDtos.AddressCreate?,
    errors: Map<String, String>,
    onAddressChange: (AddressDtos.AddressCreate) -> Unit,
    onTouched: () -> Unit = {},
) {
    val value = address ?: AddressDtos.AddressCreate(
        country = "", voivodeship = "", city = "",
        postalCode = "", street = "", houseNumber = "", apartNumber = ""
    )

    data class FieldSpec(
        val label: String,
        val errorKey: String,
        val get: (AddressDtos.AddressCreate) -> String?,
        val set: (AddressDtos.AddressCreate, String) -> AddressDtos.AddressCreate
    )

    val fields = listOf(
        FieldSpec("Country", "address.country", { it.country }) { address, new ->
            address.copy(
                country = new
            )
        },
        FieldSpec("Voivodeship", "address.voivodeship", { it.voivodeship }) { address, new ->
            address.copy(
                voivodeship = new
            )
        },
        FieldSpec("City", "address.city", { it.city }) { address, new -> address.copy(city = new) },
        FieldSpec("Postal code", "address.postalCode", { it.postalCode }) { address, new ->
            address.copy(
                postalCode = new
            )
        },
        FieldSpec(
            "Street",
            "address.street",
            { it.street }) { address, new -> address.copy(street = new) },
        FieldSpec("House number", "address.houseNumber", { it.houseNumber }) { address, new ->
            address.copy(
                houseNumber = new
            )
        },
        FieldSpec("Apartment number", "address.apartNumber", { it.apartNumber }) { address, new ->
            address.copy(
                apartNumber = new
            )
        },
    )

    fields.forEach { field ->
        AppTextField(
            value = field.get(value).orEmpty(),
            onValueChange = { new ->
                onAddressChange(field.set(value, new))
                onTouched()
            },
            label = field.label,
            errorMessage = errors[field.errorKey],
            singleLine = true
        )
    }
}