package com.example.myapplication.ui.regestration

import com.example.myapplication.util.isTextValid
import com.google.common.truth.Truth.assertThat

import org.junit.Test

class RegisterViewModelTest {


    @Test
    fun createAccountPressed() {
      val result= (!isTextValid(2, "ra"))&&(!isTextValid(6, "333"))
        assertThat(result).isFalse()

    }
}