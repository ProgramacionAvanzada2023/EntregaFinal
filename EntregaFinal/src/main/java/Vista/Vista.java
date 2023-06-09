package Vista;

import java.io.IOException;

public interface Vista {
	void recommendTitles();
	void songRecommend() throws Exception;

	void getListaCanciones() throws IOException;
}
