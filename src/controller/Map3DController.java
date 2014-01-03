package controller;

import java.io.FileNotFoundException;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

public class Map3DController implements ViewController{

	private view.Map3DView view;

	public view.Map3DView getView() {
		if (this.view == null) {
			this.view = new view.Map3DView(this);
		}
		
		return this.view;
	}

	public BranchGroup createSceneGraph(String file) {
		BranchGroup objRoot = new BranchGroup();
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
		Scene s = null;
		try {
			s = f.load(file);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
		} catch (ParsingErrorException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BoundingSphere bs = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		objRoot = s.getSceneGroup();

		Color3f bgColor = new Color3f(0.10f, 0.10f, 0.1f);
		Background bgNode = new Background(bgColor);
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

		return objRoot;
	}
}
