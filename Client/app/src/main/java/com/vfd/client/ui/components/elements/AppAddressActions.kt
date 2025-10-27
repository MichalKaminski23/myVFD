package com.vfd.client.ui.components.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
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
        FieldSpec(
            stringResource(id = R.string.country),
            "address.country",
            { it.country }) { address, new ->
            address.copy(
                country = new
            )
        },
        FieldSpec(
            stringResource(id = R.string.voivodeship),
            "address.voivodeship",
            { it.voivodeship }) { address, new ->
            address.copy(
                voivodeship = new
            )
        },
        FieldSpec(
            stringResource(id = R.string.city),
            "address.city",
            { it.city }) { address, new -> address.copy(city = new) },
        FieldSpec(
            stringResource(id = R.string.postal_code),
            "address.postalCode",
            { it.postalCode }) { address, new ->
            address.copy(
                postalCode = new
            )
        },
        FieldSpec(
            stringResource(id = R.string.street),
            "address.street",
            { it.street }) { address, new -> address.copy(street = new) },
        FieldSpec(
            stringResource(id = R.string.house_number),
            "address.houseNumber",
            { it.houseNumber }) { address, new ->
            address.copy(
                houseNumber = new
            )
        },
        FieldSpec(
            stringResource(id = R.string.apart_number),
            "address.apartNumber",
            { it.apartNumber }) { address, new ->
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