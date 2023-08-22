package vuvanhai568.project.appDocTruyen;

import java.util.List;

import vuvanhai568.project.appDocTruyen.Modal.Book;
import vuvanhai568.project.appDocTruyen.Modal.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SeverDB {
    @GET("/")
    Call<List<Book>> getAllBook();
    @GET("/Post/{id}")
    Call<List<Book>> getAllBookAcount(@Path("id") String id);
    @GET("/detail/{id}")
    Call<Book> getOneBook(@Path("id") String id);
    @POST("/comment/{id}")
    Call<Book> addComment(@Path("id") String id, @Body Book.Comment data);
    @POST("/editcomment/{idPosts}/{idPerson}/{idCommen}")
    Call<Book.Comment> editComment(@Path("idPosts") String idPosts, @Path("idPerson") String idPerson, @Path("idCommen") String idCommen, @Body Book.Comment data);
    @POST("/deletecomment/{idPosts}/{idPerson}/{idCommen}")
    Call<Book.Comment> deleteComment(@Path("idPosts") String idPosts, @Path("idPerson") String idPerson, @Path("idCommen") String idCommen);
    @POST("/createStory")
    Call<Book> postNew(@Body Book data);
    @POST("/editStory/{id}")
    Call<Book> editPost(@Path("id") String id, @Body Book data);
    @POST("/search/{id}")
    Call<List<Book>> search( @Path("id") String id);
    @POST("delete/{id}")
    Call<Book> deleteById(@Path("id") String id);
    @POST("signUp")
    Call<User> signUp(@Body User data);
    @POST("signIn")
    Call<User> signIn(@Body User data);
    @GET("/account/{id}")
    Call<User> getOnePerson(@Path("id") String id);

}
