package com.fleetio.presentation.detail

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fleetio.common.ApiResponse
import com.fleetio.domain.use_case.GetVehicleByIdUseCase
import com.fleetio.domain.use_case.QueryCommentsUseCase
import com.fleetio.presentation.VehicleCommentState
import com.fleetio.presentation.VehicleInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VehicleDetailScreenViewModel @Inject constructor(
    private val vehicleById: GetVehicleByIdUseCase,
    private val comments: QueryCommentsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _vehicle = mutableStateOf(VehicleInfoState())
    val vehicle: State<VehicleInfoState> = _vehicle

    private val _userComment = mutableStateOf(VehicleCommentState())
    val userComment: State<VehicleCommentState> = _userComment

    init {
        savedStateHandle.get<String>("id")?.let { vehicleId ->
            getVehicleById(vehicleId)
            getUserComments(vehicleId)
        }
    }

    private fun getVehicleById(id: String) {
        vehicleById(id).onEach { vehicle ->
            when(vehicle) {
                is ApiResponse.Success -> {
                    _vehicle.value = VehicleInfoState(vehicle = vehicle.data)
                }
                is ApiResponse.Error -> {
                    _vehicle.value = VehicleInfoState(apiError = vehicle.message ?: "Unexpected error occurred")
                }
                is ApiResponse.Loading -> {
                    _vehicle.value = VehicleInfoState(isLoading = true)
                }
                is ApiResponse.Exception -> {
                    _vehicle.value = VehicleInfoState(apiError = vehicle.message ?: "Unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getUserComments(vehicleId: String) {
        comments(vehicleId).onEach { comment ->
            when(comment) {
                is ApiResponse.Success -> {
                    _userComment.value = VehicleCommentState(userComments = comment.data!!)
                }
                is ApiResponse.Error -> {
                    _userComment.value = VehicleCommentState(apiError = comment.message ?: "Unexpected error occurred")
                }
                is ApiResponse.Loading -> {
                    _userComment.value = VehicleCommentState(isLoading = true)
                }
                is ApiResponse.Exception -> {
                    _userComment.value = VehicleCommentState(apiError = comment.message ?: "Unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }
}