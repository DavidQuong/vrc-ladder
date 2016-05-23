import {createElement, Element} from 'react';

const URLStyle = ({type, url}) : Element => (
   <link rel='stylesheet' type={type} href={url}/>
);

const InlineStyle = ({type, id, content}) : Element => (
  <style type={type} id={id} dangerouslySetInnerHTML={{__html: content}}/>
);

export default (props) : Element => (
  props.url ? <URLStyle {...props}/> : <InlineStyle {...props}/>
);
