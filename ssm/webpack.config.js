const path = require('path');
const webpack = require("webpack");
const htmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HappyPack = require('happypack');
const os = require('os');
const happyThreadPool = HappyPack.ThreadPool({ size: os.cpus().length });
const argv = require('minimist')(process.argv.slice(2));

const m = 'views';
const htmlTitle = '棕牛管理系统';
const iconPath = 'src/assets/favicon.ico';
const fontPath = m + '/css/font-awesome.min.css';
const functionPath = m + '/common/function.js';
const envPath = m + '/common/env.js';
const rootDir = 'webapp';

const vendor = {
  	// 'vue': 'Vue',
  	// 'element-ui': 'ElementUI',
};
function resolve(src=''){return path.join(__dirname, 'src/main/'+src)}
const plugins = [
    new htmlWebpackPlugin({
        template: resolve('src/index.html'),
        filename: 'index.html',
        title:htmlTitle,
        favicon:resolve(iconPath),
        minify:{
            //collapseWhitespace: true,
            removeComments: true,
        },
        inject:true,
        fontPath:fontPath,
        common:functionPath,
        env:envPath
    }),
    new webpack.ProgressPlugin(),
    new MiniCssExtractPlugin({
        filename: 'css/[name].css',
        chunkFilename:'css/[name].css'
    }),
    new webpack.HashedModuleIdsPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    new VueLoaderPlugin(),
    new CopyWebpackPlugin([{
        from: resolve('src/assets/fonts'),
        to: resolve(rootDir + '/' + m),
        ignore: ['.css']
    }]),
    new CopyWebpackPlugin([{
        from: resolve('src/common/function.js'),
        to: resolve(rootDir + '/' + m + '/common'),
    }]),
    new CopyWebpackPlugin([{
        from: resolve('src/common/env.js'),
        to: resolve(rootDir + '/' + m + '/common'),
    }]),
    new HappyPack({
        id: 'happybabelJs',
        loaders: [{loader:'babel-loader',options:{cacheDirectory:true}}],
        threadPool: happyThreadPool,
        verbose: true
    }),
    new HappyPack({
        id: 'happybabelCss',
        loaders: [
            {loader:'css-loader'/*,options:{minimize:true,cacheDirectory:true}*/}
        ],
        threadPool: happyThreadPool,
        verbose: true
    })
];
(argv.env == 'production')&&(plugins.push(new CleanWebpackPlugin()));
module.exports = {
    entry: {
    	main:resolve('src/main.js')
    },
    output: {
        path: resolve(rootDir + '/' + m),
        chunkFilename: 'js/[name].js',
        filename: 'js/[name].js',
        publicPath: '/' + m + '/'
    },
    externals: vendor,
    devServer: {
        contentBase: resolve(rootDir + '/' + m),
        host: '127.0.0.1',
        port: 3000,
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
        hot: true,
        inline: true,
        compress:true,
    },
    performance: {
        hints: "warning",
        maxAssetSize: 30000000,
        maxEntrypointSize: 50000000,
        assetFilter: function(assetFilename) {
            return assetFilename.endsWith('.css') || assetFilename.endsWith('.js');
        }
    },
    module: {
        rules: [{
            test: /\.css$/,
            use:[MiniCssExtractPlugin.loader,'css-loader',
            	{loader:'postcss-loader',options:{
            		indet:'postcss',
            		sourceMap: true,
            		plugins:[require('autoprefixer')({
        				overrideBrowserslist : ['last 100 versions']
            		})]
            }}]
        }, {
            test: /\.less$/,
            use:['style-loader','happypack/loader?id=happybabelCss',
                {loader:'postcss-loader',options:{
                    indet:'postcss',
                    sourceMap: true,
                    plugins:[require('autoprefixer')({
                        overrideBrowserslist : ['last 100 versions']
                    })]
            }},'less-loader']
        }, {
            test: /\.(scss|sass)$/,
            use:['style-loader','happypack/loader?id=happybabelCss',
                {loader:'postcss-loader',options:{
                        indet:'postcss',
                        sourceMap: true,
                        plugins:[require('autoprefixer')({
                            overrideBrowserslist : ['last 100 versions']
                        })]
            }},'sass-loader']
        }, {
            test: /\.styl(us)$/,
            use: ['style-loader', 'happypack/loader?id=happybabelCss',
                {loader:'postcss-loader',options:{
                    indet:'postcss',
                    sourceMap: true,
                    plugins:[require('autoprefixer')({
                        overrideBrowserslist : ['last 100 versions']
                    })]
            }},'stylus-loader']
        }, {
            test: /\.(jpg|png|gif|bmp|jpeg)(\?.*)?$/i,
            use: ['url-loader?limit=7631&name=images/[hash:8]-[name].[ext]',{
            	loader:'image-webpack-loader',
            	options:{bypassOnDebug: true}
            }]
        }, {
            test: /\.(ttf|eot|svg|woff|woff2)(\?.*)?$/,
            use: 'url-loader?limit=10000&name=fonts/[hash:8]-[name].[ext]'
        }, {
            test: /\.(js|jsx)$/,
            use: 'happypack/loader?id=happybabelJs',
            include: resolve('src'),
            exclude: '/node_modules/'
        }, {
            test: /\.vue$/,
            use: 'vue-loader'
        }, /*{
          test: /\.tsx?$/,
          use: 'ts-loader',
          exclude: /node_modules/
        },*/]
    },
    plugins:plugins,
    optimization: {
	    runtimeChunk: {
	      	name: 'manifest'
	    },
	    minimizer: [
            new UglifyJsPlugin({
                cache: true,
                parallel: true,
                sourceMap: true
            }),
            new OptimizeCSSAssetsPlugin({
            	assetNameRegExp: /\.css$/g,
                cssProcessor: require('cssnano'),
                cssProcessorPluginOptions: {
					preset: ['default', {
				        discardComments: {
				            removeAll: true,
				        },
				        normalizeUnicode: false
				    }]
				 },
                canPrint: true
            })
	    ], // [new UglifyJsPlugin({...})]
	    splitChunks:{
	      	chunks: 'async',
	      	minSize: 30000,
	      	minChunks: 1,
	      	maxAsyncRequests: 5,
	      	maxInitialRequests: 3,
	      	name: false,
	      	cacheGroups: {
		        vendor: {
		          name: 'vendor',
		          chunks: 'initial',
		          priority: -10,
		          reuseExistingChunk: false,
		          test: /node_modules\/(.*)\.js/
		        },
		        styles: {
		          name: 'styles',
		          test: /\.(scss|css)$/,
		          chunks: 'all',
		          minChunks: 1,
		          reuseExistingChunk: true,
		          enforce: true
		        },
		        commons: {
                    name: "commons",
                    chunks: "initial",
                    minChunks: 2
                }
	      	}
	    }
	},
    resolve: {
        extensions: ['.tsx','.ts','.js','.vue','.json'],
        alias: {
            "vue$": "vue/dist/vue.js",
            "@": resolve('src')
        }
    }
};