package view;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.swing.JPanel;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Map3DView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.Map3DController controller;
	
	private SimpleUniverse su;
	
	public Map3DView(controller.Map3DController controller) {
		setLayout(new BorderLayout());
		
		this.controller = controller;
		Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		add(c);
		c.setDoubleBufferEnable(true);
		su = new SimpleUniverse(c);
		BranchGroup scene;
		try {
			scene = controller.createSceneGraph("./3d/quadrokopter.obj", "./3d/landschaft.jpg");
			
			OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.DISABLE_ROTATE | OrbitBehavior.DISABLE_TRANSLATE);
			orbit.setSchedulingBounds(new BoundingSphere());
			su.getViewingPlatform().setViewPlatformBehavior(orbit);
			
			scene.compile();
			su.addBranchGraph(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMetaData(model.MetaData metaData) {

	}
}
