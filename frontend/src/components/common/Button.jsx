export default function Button({ children, icon: Icon, variant = 'primary', className = '', ...props }) {
  return (
    <button className={`btn btn-${variant} ${className}`} {...props}>
      {Icon && <Icon size={18} aria-hidden="true" />}
      <span>{children}</span>
    </button>
  )
}
