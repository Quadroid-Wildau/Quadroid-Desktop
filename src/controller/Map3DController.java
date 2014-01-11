package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import service.MetaDataService;
import view.Map3DView;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.image.TextureLoader;

import controller.interfaces.ViewController;

import de.th_wildau.quadroid.models.MetaData;

/**
 * Controller for the 3D Model view
 * @author Georg Baumgarten
 * @version 1.0
 *
 */
public class Map3DController implements ViewController, Observer {

	private Map3DView view;

	public Map3DView getView() {
		if (this.view == null) {
			this.view = new Map3DView(this);
		}
		
		//Observer for Metadata to get yaw pitch roll
		MetaDataService.getInstance().addObserver(this);
		return this.view;
	}

	/**
	 * Create the scene graph for the 3D Model
	 * @param file
	 * 			.obj file (model) to load
	 * @param backgroundFile
	 * 			Background image file (image file)
	 * @return
	 * 			The Scenegraph ( {@link BranchGroup} )
	 * @throws IOException
	 */
	public BranchGroup createSceneGraph(String file, String backgroundFile) throws IOException {
		BranchGroup objRoot = new BranchGroup();
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
		Scene s = null;
		try {
			s = f.load(getClass().getResource(file));
		} catch (IncorrectFormatException e) {
			System.err.println(e);
		} catch (ParsingErrorException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BoundingSphere bs = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		objRoot = s.getSceneGroup();

		BufferedImage image = ImageIO.read(getClass().getResourceAsStream(backgroundFile));
		ImageComponent2D bgImage = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA8, image);
		Background bgNode = new Background(bgImage);
		bgNode.setImageScaleMode(Background.SCALE_FIT_ALL);
		bgNode.setApplicationBounds(bs);
		objRoot.addChild(bgNode);
		
		// Licht
		DirectionalLight d1 = new DirectionalLight();
		d1.setInfluencingBounds(new BoundingSphere());
		d1.setColor(new Color3f(1.0f, 1.0f, 1.0f));
		objRoot.addChild(d1);

		DirectionalLight d2 = new DirectionalLight();
		d2.setInfluencingBounds(new BoundingSphere());
		d2.setDirection(new Vector3f(1.0f, 1.0f, 0.0f));
		d2.setColor(new Color3f(1.0f, 1.0f, 1.0f));
		objRoot.addChild(d2);

		DirectionalLight d3 = new DirectionalLight();
		d3.setInfluencingBounds(new BoundingSphere());
		d3.setDirection(new Vector3f(-1.0f, -1.0f, -1.0f));
		d3.setColor(new Color3f(1.0f, 1.0f, 1.0f));
		objRoot.addChild(d3);

		Shape3D node = (Shape3D) objRoot.getChild(5);
		Appearance app = new Appearance();
		Color3f col = new Color3f(Color.red);
		ColoringAttributes color1ca = new ColoringAttributes (col, 1);
		app.setColoringAttributes(color1ca);
		node.setAppearance(app);
		 
		//Because the texture loader of .obj files, which should load .mtl files in the same directory,
		//is not working, we have to apply textures and colors manually
		Texture metalTexture = new TextureLoader(getClass().getResource("/model3d/metal.jpg"), view).getTexture();
		Appearance metalApperance = new Appearance();
		metalApperance.setTexture(metalTexture);
		
		((Shape3D) objRoot.getChild(0)).setAppearance(metalApperance);
		((Shape3D) objRoot.getChild(2)).setAppearance(metalApperance);
		((Shape3D) objRoot.getChild(4)).setAppearance(metalApperance);
		((Shape3D) objRoot.getChild(7)).setAppearance(metalApperance);
		((Shape3D) objRoot.getChild(6)).setAppearance(metalApperance);
		((Shape3D) objRoot.getChild(3)).setAppearance(metalApperance);
		
		return objRoot;
	}

	@Override
	public void update(Observable observable, Object obj) {
		if (observable instanceof MetaDataService) {
			MetaData metadata = (MetaData) obj;
			getView().setMetaData(metadata);
		}
	}
}
