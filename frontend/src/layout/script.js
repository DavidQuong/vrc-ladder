import {createElement, Element} from 'react';

const URLScript = ({type, url}) : Element => (
   <script type={type} src={url}/>
);

const InlineScript = ({type, id, content}) : Element => (
  <script type={type} id={id} dangerouslySetInnerHTML={{__html: content}}/>
);

export default (props) : Element => (
  props.url ? <URLScript {...props}/> : <InlineScript {...props}/>
);
