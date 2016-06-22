/**
 * Class for manage Assets.
 * 
 * For include new assets follow the next steps:
 * 1. Create a new public class inside this class. Example
 *	 public class AssetBackground{
			public final AtlasRegion sky;
			public final AtlasRegion fence;
			
			public AssetBackground(TextureAtlas atlas){
				sky = atlas.findRegion("sky");
				fence = atlas.findRegion("fence");
			}
		}
 * 2. Create a varible for the class. Example: public AssetBackground background;
 * 3. Inside create method, create the class object. Example background = new AssetBackground(atlas1);
 */
package com.epifania.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Locale;

public class Assets implements Disposable, AssetErrorListener{

	private static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	
	public ValAssets valAssets;
	public GomhAnimations gomhAnimations;
	public Items items;
	
	private Assets(){}
	
	public void init(AssetManager assetManager){
		this.assetManager = assetManager;
		Texture.setAssetManager(this.assetManager);
		assetManager.setErrorListener(this);

		//Load fonts
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class,new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class,new FreetypeFontLoader(resolver));
		assetManager.setLoader(TiledMap.class,new TmxMapLoader(resolver));

		FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter.fontFileName = "user interface/Lobster.ttf";
		parameter.fontParameters.size = 25;
		parameter.fontParameters.magFilter = Texture.TextureFilter.Linear;
		parameter.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter.fontParameters.genMipMaps = true;
		this.assetManager.load("gameFont.fnt",BitmapFont.class,parameter);
		FreetypeFontLoader.FreeTypeFontLoaderParameter parameter2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter2.fontFileName = "user interface/Lobster.ttf";
		parameter2.fontParameters.size = 50;
		parameter2.fontParameters.magFilter = Texture.TextureFilter.Linear;
		parameter2.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter2.fontParameters.genMipMaps = true;
		this.assetManager.load("bigFont.fnt",BitmapFont.class,parameter2);
		FreetypeFontLoader.FreeTypeFontLoaderParameter parameter3= new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter3.fontFileName = "user interface/Lobster.ttf";
		parameter3.fontParameters.size = 40;
		parameter3.fontParameters.magFilter = Texture.TextureFilter.Linear;
		parameter3.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter3.fontParameters.genMipMaps = true;
		this.assetManager.load("midFont.fnt",BitmapFont.class,parameter3);
		FreetypeFontLoader.FreeTypeFontLoaderParameter parameter4= new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter4.fontFileName = "user interface/Lobster.ttf";
		parameter4.fontParameters.size = 40;
		parameter4.fontParameters.borderColor = Color.GRAY;
		parameter4.fontParameters.borderWidth = 3;
		parameter4.fontParameters.magFilter = Texture.TextureFilter.Linear;
		parameter4.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter4.fontParameters.genMipMaps = true;
		this.assetManager.load("midFont_outline.fnt",BitmapFont.class,parameter4);
		FreetypeFontLoader.FreeTypeFontLoaderParameter parameter5= new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		parameter5.fontFileName = "user interface/Lobster.ttf";
		parameter5.fontParameters.size = 40;
		parameter5.fontParameters.borderColor = new Color(0xc83737ff);
		parameter5.fontParameters.borderWidth = 3;
		parameter5.fontParameters.magFilter = Texture.TextureFilter.Linear;
		parameter5.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter5.fontParameters.genMipMaps = true;
		this.assetManager.load("midFont_negative.fnt",BitmapFont.class,parameter5);

		assetManager.load("user interface/uiskin.atlas", TextureAtlas.class);

		//i18n
		assetManager.load("i18n/strings_ui", I18NBundle.class);

		assetManager.finishLoading();

		//Load resources
		Assets.instance.load("characters/characters.atlas",TextureAtlas.class);
		Assets.instance.load("game_objects/items.atlas",TextureAtlas.class);
		assetManager.load("sounds/click1.ogg",Sound.class);

		//Load skin
		//TODO solve issue of bitmap font in skin and free type font

		BitmapFont fontHeader = generateFont("user interface/Tinos-Regular.ttf",50);
		BitmapFont fontTitle = generateFont("user interface/Cinzel_Regular.ttf",90);

		String textureAtlasPath = "user interface/ninePatches.atlas";
		ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
		BitmapFont f=assetManager.get("gameFont.fnt",BitmapFont.class);
		BitmapFont skinFont = new BitmapFont(f.getData(),f.getRegions(),true);
		BitmapFont f2 = assetManager.get("bigFont.fnt",BitmapFont.class);
		BitmapFont skinFont2 = new BitmapFont(f2.getData(),f2.getRegions(),true);
		BitmapFont f3 = assetManager.get("midFont.fnt",BitmapFont.class);
		BitmapFont skinFont3 = new BitmapFont(f3.getData(),f3.getRegions(),true);
		BitmapFont f4 = assetManager.get("midFont_outline.fnt",BitmapFont.class);
		BitmapFont skinFont4 = new BitmapFont(f4.getData(),f4.getRegions(),true);
		BitmapFont f5 = assetManager.get("midFont_negative.fnt",BitmapFont.class);
		BitmapFont skinFont5 = new BitmapFont(f5.getData(),f5.getRegions(),true);
		resources.put("default",skinFont);
		resources.put("title",skinFont2);
		resources.put("middle",skinFont3);
		resources.put("middle_outline",skinFont4);
		resources.put("middle_negative",skinFont5);
		resources.put("header",fontHeader);
		resources.put("h1title",fontTitle);
		TextureAtlas atlas = assetManager.get("user interface/uiskin.atlas", TextureAtlas.class);
		for(int i = 0; i <atlas.getRegions().size;i++){
			TextureAtlas.AtlasRegion region = atlas.getRegions().get(i);
			resources.put(region.name,new TextureRegion(region));
		}
		SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter(textureAtlasPath,resources);
		assetManager.load("user interface/uiskin.json", Skin.class,skinParameter);
		assetManager.finishLoading();
		valAssets = new ValAssets(assetManager.get("characters/characters.atlas",TextureAtlas.class));
		gomhAnimations = new GomhAnimations(assetManager.get("characters/characters.atlas",TextureAtlas.class));
		items = new Items(assetManager.get("game_objects/items.atlas",TextureAtlas.class));
	}

	public BitmapFont generateFont(String path, int size){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = size;
		param.minFilter = Texture.TextureFilter.MipMapLinearNearest;
		param.magFilter = Texture.TextureFilter.Linear;
		param.genMipMaps = true;
		BitmapFont font = generator.generateFont(param);
		generator.dispose();
		return font;
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Could not load asset '"+asset.fileName+"'",(Exception)throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		Gdx.app.debug(TAG,"asset manager has been disposed");
	}

	public void unload(String name){
		assetManager.unload(name);
	}

	public void load(String name,Class type){
		assetManager.load(name,type);
	}

	public void load(String name, Class type,AssetLoaderParameters parameters){
		assetManager.load(name,type,parameters);
	}

	public void finishLoading(){
		assetManager.finishLoading();
	}

	public boolean update(){
		return assetManager.update();
	}

	public synchronized <T> T get(String fileName) {
		return assetManager.get(fileName);
	}

	public synchronized <T> T get(String fileName, Class<T> type) {
		return assetManager.get(fileName, type);
	}
	
	public class ValAssets{
		public final Animation hurt;
		public final Animation jump;
		public final Animation walkRight;
		public final Animation walkLeft;
		public final Animation idle;
		public final Animation climb;

		public ValAssets(TextureAtlas atlas){
			//TODO Climb Animation
			float frameDuration = 1f;
			Array<TextureRegion> frames = new Array<TextureRegion>();
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_walk1")));
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_walk2")));
			walkRight = new Animation(frameDuration*0.1f,frames,PlayMode.LOOP);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_walk3")));
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_walk4")));
			walkLeft = new Animation(frameDuration*0.1f,frames,PlayMode.LOOP);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_hurt")));
			hurt =  new Animation(frameDuration,frames,PlayMode.LOOP);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_jump")));
			jump =  new Animation(frameDuration,frames,PlayMode.LOOP);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_climb1")));
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_climb2")));
			climb =  new Animation(frameDuration*0.2f,frames,PlayMode.LOOP);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_stand")));
			frames.add(new TextureRegion(atlas.findRegion("Val/bunny2_ready")));
			idle =  new Animation(frameDuration,frames,PlayMode.LOOP);
		}
	}

	public class GomhAnimations{
		public  final Animation left;
		public  final Animation right;
		public  final Animation center;
		public GomhAnimations(TextureAtlas atlas){
			float frameDuration = 1.0f;
			Array<TextureRegion> frames = new Array<TextureRegion>();
			frames.add(new TextureRegion(atlas.findRegion("Gomh/gomhLeft")));
			left = new Animation(frameDuration,frames,PlayMode.NORMAL);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Gomh/gomhRight")));
			right = new Animation(frameDuration,frames,PlayMode.NORMAL);
			frames.clear();
			frames.add(new TextureRegion(atlas.findRegion("Gomh/gomhIdle")));
			center = new Animation(frameDuration,frames,PlayMode.NORMAL);
		}
	}

	public class Items{
		public final Animation coin_gold;
		public final Animation coin_silver;
		public final Animation coin_bronze;

		public Items(TextureAtlas atlas){
			float frameDuration = 0.15f;
			Array<TextureRegion> frames = new Array<TextureRegion>();
			for(int index = 1;index<=4;index++){
				frames.add(atlas.findRegion("gold",index));
			}

			for(int i = 3;i>=2;i--){
				TextureRegion region = new TextureRegion(atlas.findRegion("gold",i));
				region.flip(true,false);
				frames.add(region);
			}
			coin_gold = new Animation(frameDuration,frames,PlayMode.LOOP_REVERSED);

			frames.clear();
			frames = new Array<TextureRegion>();
			for(int index = 1;index<=4;index++){
				frames.add(atlas.findRegion("silver",index));
			}
			for(int i = 3;i>=2;i--){
				TextureRegion region = new TextureRegion(atlas.findRegion("silver",i));
				region.flip(true,false);
				frames.add(region);
			}
			coin_silver = new Animation(frameDuration,frames,PlayMode.LOOP_REVERSED);

			frames.clear();
			frames = new Array<TextureRegion>();
			for(int index = 1;index<=4;index++){
				frames.add(atlas.findRegion("bronze",index));
			}
			for(int i = 3;i>=2;i--){
				TextureRegion region = new TextureRegion(atlas.findRegion("bronze",i));
				region.flip(true,false);
				frames.add(region);
			}
			coin_bronze = new Animation(frameDuration,frames,PlayMode.LOOP_REVERSED);
			frames.clear();

		}
	}
}