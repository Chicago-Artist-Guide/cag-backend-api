import com.cag.cagbackendapi.utilities.impl.ProfileUtilities
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger

@ExtendWith(MockitoExtension::class)
internal class ProfileUtilitiesTest {

    @Mock
    private lateinit var logger: Logger

    @Test
    fun getRandomEmailTest() {
        var testEmail = ProfileUtilities.randomEmail()
        var testEmail2 = ProfileUtilities.randomEmail()

        assertNotEquals(testEmail, testEmail2)
    }
}
