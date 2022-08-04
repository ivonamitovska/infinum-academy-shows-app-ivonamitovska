import com.infinum.shows_ivona_mitovska.data.response.ShowsResponse
import com.infinum.shows_ivona_mitovska.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    @SerialName("user") val user: User
)

