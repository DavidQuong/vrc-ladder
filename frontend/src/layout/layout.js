import {createElement, Element} from 'react';

import Script from './script';
import Style from './style';

export default ({
  markup,
  scripts,
  styles,
  title,
  locale,
}) : Element => (
  <html lang={locale}>
    <head>
      <title>{title}</title>
      {styles.map((style, i) => <Style key={i} {...style}/>)}
    </head>
    <body>
      <div id='content' dangerouslySetInnerHTML={{__html: markup}}/>
      {scripts.map((script, i) => <Script key={i} {...script}/>)}
    </body>
  </html>
);
