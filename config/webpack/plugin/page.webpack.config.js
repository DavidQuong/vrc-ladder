import collect from 'webpack-assets';
import RawSource from 'webpack/lib/RawSource';
import {renderToStaticMarkup} from 'react-dom/server';
import {createElement} from 'react';

export default class PagePlugin {
  constructor({component, output = 'index.html'}) {
    this.component = component;
    this.output = output;
  }
  apply(compiler) {
    compiler.plugin('emit', (compilation, done) => {
      const assets = collect(compilation.getStats().toJson());
      const output = renderToStaticMarkup(createElement(
        this.component,
        {assets}
      ));
      compilation.assets[this.output] = new RawSource(output);
      done();
    });
  }
}
