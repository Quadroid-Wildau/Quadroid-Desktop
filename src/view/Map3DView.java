package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

import de.th_wildau.quadroid.models.Attitude;
import de.th_wildau.quadroid.models.MetaData;

public class Map3DView extends JPanel{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private controller.Map3DController controller;
	
	private SimpleUniverse mSimpleUniverse;
	
	public Map3DView(controller.Map3DController controller) {
		setPreferredSize(new Dimension(500, 350));
		setMinimumSize(new Dimension(200, 200));
		setMaximumSize(new Dimension(500, 300));
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setDoubleBuffered(true);
		
		JLabel lblMetadaten = new JLabel("3D Modell");
		lblMetadaten.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 3)));
		lblMetadaten.setFont(new Font("Tahoma", Font.BOLD, 18));
		add(lblMetadaten, BorderLayout.NORTH);
		
		this.controller = controller;
		Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		add(c, BorderLayout.CENTER);
		
		c.setDoubleBufferEnable(true);
		mSimpleUniverse = new SimpleUniverse(c);
		BranchGroup scene;
		try {
			scene = controller.createSceneGraph("./3d/quadrokopter.obj", "./3d/landschaft.jpg");
			
			OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.DISABLE_ROTATE | OrbitBehavior.DISABLE_TRANSLATE);
			orbit.setSchedulingBounds(new BoundingSphere());
			mSimpleUniverse.getViewingPlatform().setViewPlatformBehavior(orbit);
			
			Transform3D t = new Transform3D();
			Transform3D rotate = getTransform3DRotation(0, 1, 0.5, 60);
			rotate.mul(getTransform3DTranslation(0, 0, 3.5));
			t.mul(rotate);
			
			mSimpleUniverse.getViewingPlatform().getViewPlatformTransform().setTransform(t);
			
			scene.compile();
			mSimpleUniverse.addBranchGraph(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Transform3D lookTowardsOriginFrom(Point3d point) {
        Transform3D move = new Transform3D();
        Vector3d up = new Vector3d(point.x, point.y + 1, point.z);
        move.lookAt(point, new Point3d(0.0d, 0.0d, 0.0d), up);

        return move;
    }
	 
	private Transform3D getTransform3DTranslation(double x, double y, double z) {
		Transform3D t = new Transform3D();
		t.setTranslation(new Vector3d(x, y, z));
		return t;
	}
	
	private Transform3D getTransform3DRotation(double x, double y, double z, double angle) {
		Transform3D t = new Transform3D();
		t.setRotation(new AxisAngle4d(x, y, z, angle));
		return t;
	}

	public void setMetaData(MetaData metaData) {
		Attitude att = metaData.getAttitude();
		
		Transform3D t = new Transform3D();
		Transform3D mRollTransform = getTransform3DRotation(0, 0, 1, att.getRoll());
		Transform3D mYawTransform = getTransform3DRotation(0, 1, 0, att.getYaw());
		Transform3D mPitchTransform = getTransform3DRotation(1, 0, 0, att.getPitch());
		Transform3D mZoomOut = getTransform3DTranslation(0, 0, 3.5);
		
		t.mul(mRollTransform);
		t.mul(mYawTransform);
		t.mul(mPitchTransform);
		t.mul(mZoomOut);
		
		mSimpleUniverse.getViewingPlatform().getViewPlatformTransform().setTransform(t);
	}
}
