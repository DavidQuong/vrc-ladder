import compose from 'lodash/fp/flowRight';
// import identity from 'lodash/fp/identity';
import filter from 'lodash/fp/filter';
import {createElement} from 'react';

// Dealing with bundled code.
import sourceMaps from 'webpack-config-source-maps';
// import optimize from 'webpack-config-optimize';

// Basics.
import entry from 'webpack-config-entry';
import {plugin, output} from 'webpack-partial';

// For importing filetypes.
import postcss from 'webpack-config-postcss';
import babel from 'webpack-config-babel';
import json from 'webpack-config-json';

// For uploading the generated website somewhere.
// import S3Plugin from './plugin/s3.webpack.config';

// For generating the actual HTML output.
import PagePlugin from './plugin/page.webpack.config';
import Layout from '../../src/layout/layout';

// For putting assets into the page.
const scripts = filter({contentType: 'application/javascript'});
const styles = filter({contentType: 'text/css'});

const formats = compose(
  babel(), // .js
  json(), // .json
  postcss() // .css
);

export default compose(
  // env('production', optimize()),
  sourceMaps(),
  formats,
  output({publicPath: '/'}),
  entry({name: 'web'}),
  plugin(new PagePlugin({
    output: 'index.html',
    component: ({assets}) => (
      <Layout
        styles={styles(assets)}
        scripts={scripts(assets)}
        title='VRC Badminton Ladder'
        locale='en-US'
      />
    ),
  }))
  // plugin(new S3Plugin('s3://izaakschroeder-test/'))
)({target: 'web'});
