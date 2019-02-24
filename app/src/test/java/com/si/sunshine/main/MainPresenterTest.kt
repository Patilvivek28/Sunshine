package com.si.sunshine.main

import com.si.sunshine.main.components.JsonWeatherTask
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MainPresenterTest {

    @Mock
    private lateinit var view: MainContract.View
    @Mock
    private lateinit var weatherTask: JsonWeatherTask

    private lateinit var presenter: MainPresenter

    @Before
    fun `Setup method`() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view, weatherTask)
    }

    @Test
    fun `Verify setPresenter method`() {
        verify(view).setPresenter(presenter)
    }

    @Test
    fun `Test start method`() {
        presenter.start()

        verify(weatherTask).setListener(presenter)
    }

    @Test
    fun `Test processDate method`() {
        presenter.processDate(TEST_DAY, TEST_MONTH, TEST_YEAR)

        verify(weatherTask).execute(TEST_UNIX_TIME)
    }

    @Test
    fun `Test processDate method DAY_NON_PRIME`() {
        presenter.processDate(TEST_DAY_NON_PRIME, TEST_MONTH, TEST_YEAR)

        verify(view, never()).onPreExecute()
    }

    @Test
    fun `Test onPreExecute method`() {
        presenter.onPreExecute()

        verify(view).onPreExecute()
    }

    @Test
    fun `Test onPostExecute method`() {
        presenter.onPostExecute(TEST_TEMPERATURE, TEST_TEMPERATURE)

        verify(view).onPostExecute(TEST_TEMPERATURE, TEST_TEMPERATURE)
    }

    companion object {
        private const val TEST_DAY = 7
        private const val TEST_DAY_NON_PRIME = 12
        private const val TEST_MONTH = 2
        private const val TEST_YEAR = 2019

        private const val TEST_UNIX_TIME: Long = 1551897000
        private const val TEST_TEMPERATURE = "32 °C"
    }
}